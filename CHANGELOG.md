# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [4.0.0]
### Changed
- Moved `super.bindItem` call to the end of `ItemViewHolder#bindItem`method so that it's possible to override values set by it in callbacks.
- *Breaking change:* updated `MaterialPopupMenuBuilder.SectionHolder.label` and `MaterialPopupMenuBuilder.ItemHolder.label` to be of type `CharSequence` rather than `String` to allow the use of `Spannables`.

## [3.4.0]
### Added
- An option to display an icon at the end of each item which indicates a nested submenu.
- `viewBoundCallback` added to default items as well
- Allow customizing default popup style via theme attribute (#53, thanks to @Tunous)
- Make it possible to dismiss popup from `viewBoundCallback` (#57, thanks to @Tunous)

### Fixed
- Disabled `clipToPadding` on menu `RecyclerView ` (#54, thanks to @Tunous)
- Take margins under account when calculating popup height (#56, thanks to @Tunous)

## [3.3.0]
### Added
- An option to keep popup open after clicking on item (#45, thanks to @Tunous)
- Allow setting item label via string resource (#42, thanks to @Tunous)
- A flag to dim background behind popup (#43, thanks to @Tunous)
- A way to customize popup padding (#44, thanks to @Tunous)

### Changed
- new project & sample app logos (#48, thanks to @ghostofiht)

### Fixed
- Incorrect menu height in case of dynamic content (#49, reported by @Tunous)
- Fix background clipping when using rounded corners (#47, thanks to @Tunous)

## [3.2.0]
### Changed
- Updated Kotlin to 1.3.21, AndroidX AppCompat to 1.0.2
- Updated internal AGP to 3.3.2 & Gradle to 5.3.1

## [3.1.0]
### Added
- A listener to get notified when popup menus get dismissed

## [3.0.0]
### Changed
- Migrated to AndroidX package structure (https://developer.android.com/topic/libraries/support-library/androidx-overview)
- Updated Kotlin to 1.2.71

## [2.2.0]
### Added
- A way to set custom vertical offsets via android:dropDownVerticalOffset and by setting Gravity.BOTTOM

### Changed
- Updated Android Support Library to 27.1.1
- Updated Kotlin to 1.2.61
- Changed kotlin-stdlib-jre7 dependency to kotlin-stdlib-jdk7

## [2.1.0]
### Added
- A way to dismiss popup menus programmatically

## [2.0.0]
### Fixed
- Overriding of Android Themes as raised by @plshapkin in #18. This introduces breaking changes hence 2.0.0 version update.

## [1.4.2]
### Changed
- Updated Android Support Library to 27.1.0
- Updated Kotlin to 1.2.31

## [1.4.1]
### Fixed
- Order of items ruined if menu has to scroll (issue #14)

## [1.4.0]
### Added
- A way to set background color of the entire popup window

### Changed
- Light & dark themes use correct backgrounds & selectors by default without the need of setting an Activity theme.

## [1.3.0]
### Added
- A way to set an icon Drawable for an item

## [1.2.0]
### Added
- A way to set custom items

## [1.1.0]
### Added
- A way to set individual item label & icon colors.

### Changed
- Compilation against Oreo MR1
- Updated a bunch of dependencies including Android Support Library to 27.0.2 & Kotlin to 1.2.20

## [1.0.0]
### Added
Initial release of the library.

[Unreleased]: https://github.com/zawadz88/MaterialPopupMenu/compare/4.0.0...HEAD
[4.0.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/3.4.0...4.0.0
[3.4.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/3.3.0...3.4.0
[3.3.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/3.2.0...3.3.0
[3.2.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/3.1.0...3.2.0
[3.1.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/3.0.0...3.1.0
[3.0.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/2.2.0...3.0.0
[2.2.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/2.1.0...2.2.0
[2.1.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/2.0.0...2.1.0
[2.0.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/1.4.2...2.0.0
[1.4.2]: https://github.com/zawadz88/MaterialPopupMenu/compare/1.4.1...1.4.2
[1.4.1]: https://github.com/zawadz88/MaterialPopupMenu/compare/1.4.0...1.4.1
[1.4.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/1.3.0...1.4.0
[1.3.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/1.2.0...1.3.0
[1.2.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/1.1.0...1.2.0
[1.1.0]: https://github.com/zawadz88/MaterialPopupMenu/compare/1.0.0...1.1.0
[1.0.0]: https://github.com/zawadz88/MaterialPopupMenu/tree/1.0.0
