# Architecture notes

## Repository granularity

The code review suggested collapsing the three single-function repository
interfaces — `GetCharactersRepository.fetchCharacters()`,
`SearchCharacterRepository.searchCharacters(name)` and
`CharacterDetailsRepository.requestCharacterDetails(id)` — into one
`CharacterRepository` with three functions. All three are backed by the same
Retrofit service and the same paging setup, so one interface would mean one
file, one Hilt binding and one implementation instead of three of each.

I am keeping them separate.

**Reasoning.** Interface segregation: a use case depends only on the operation it
actually calls, so a change to the search contract cannot force a recompile or a
reread of the details path. Test doubles stay trivial — a fake implements one
function, with no `TODO()` stubs for the two functions the test does not
exercise, and no risk of a stub being hit by accident.

**Cost.** Three files instead of one, three `@Binds` declarations instead of one,
and a reader has to open three files to see the full data surface. That is real
overhead and it grows with each new operation.

**When I would change my mind.** If the three implementations start sharing
mutable state — a cache, a session, an in-memory page buffer — the split becomes
a lie about how the code is actually organised, and I would merge them.

## Compose stability across module boundaries

The review comment was that the Home screen's UI state class is unstable, and
that this stops the list from skipping recomposition.

The compiler report says otherwise. `HomeUiState` is stable; the unstable
parameter was `CharacterModel` (then named `CharacterUIModel`) at the
`CharacterItem` call site:

```
stable class com.droidos.home.uiState.HomeUiState {
  stable val searchQuery: String
  <runtime stability> = Stable
}
```
```
restartable skippable fun com.droidos.home.CharacterItem(
  unstable uiState: CharacterUIModel
```

The class itself was fine — five `val`s of `Int`/`String`. The cause is that
`:core:model` is a plain `java-library` module with no Compose compiler plugin,
so its bytecode carries no `@StabilityInferred` metadata and every consumer
falls back to "unstable" regardless of what the properties are.

Strong skipping is on, so `CharacterItem` stayed `skippable` throughout and
simply fell back to instance equality for that parameter. The practical runtime
cost was modest: the skip is only lost when Paging rebuilds items, on refresh or
on a new search query.

**Fix: `@Immutable` on the class.** Cost: `:core:model` now depends on
`androidx.compose.runtime` (`compileOnly`), and the annotation is an assertion,
not an inference — it silently becomes false the moment someone adds a `var` or
an unstable property, with no compiler error.

**When this flips:** if `:core:model` grows to many entity types, apply
`kotlin.plugin.compose` to the module instead. Inferred stability cannot rot the
way a hand-written annotation can.
