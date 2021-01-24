## Getting Started

Some library project using :

## Bloc / Flutter_Bloc : An Architecture MVVM for flutter. Read more in here :
https://bloclibrary.dev/#/

## Sembast database library : NoSQL database using library sembast to save data in local database
https://pub.dev/packages/sembast

## Multi-languages : Using easy_localization with multi-languages :
https://pub.dev/packages/easy_localization

Note : I've create class LanguagesKey in file multi_languages_key.dart in utils folder.
This file will help us to define key we've using in languages.json file to not mistake when input key data.
    REMEMBER when you create another key-value in languages.json -> Add this key to LanguagesKey class

## Json_serializable : Use to generate a model class to Json data with function fromJson and toJson automatically without cost time to code
https://pub.dev/packages/json_serializable

2 terminal need to remind :
    - flutter pub run build_runner build : Use to generate file you've define in model class
    -  flutter pub run build_runner build --delete-conflicting-outputs : Use to generate new file and delete old conflicting file you've already generate.
     Use in case terminal 1 is error

## Coding convention - Naming convention : Use pedantic package to auto-check coding convention and naming convention when code
https://dart.dev/guides/language/analysis-options

## Get-it : Dependencies Injector -> Using like provider but without context. Just only registry first time in main class and use every where
https://pub.dev/packages/get_it



