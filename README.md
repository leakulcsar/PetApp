# Pet Finder app

This simple app contains 2 screens:
* [HomeScreen](java/com/example/petapp/feature/home/HomeScreen.kt):
    * Responsible for displaying the animal list from PetFinder
* [DetailScreen](java/com/example/petapp/feature/detail/DetailScreen.kt):
    * Displays the details of a pet once selected

The app is built using a layered architecture where we have the:
* presentation layer - responsible for retrieving the necessary data from domain for the UI and displaying it
* domain layer - defines the business logic domain of the app
* data layer - provides the data implementation details needed for by the domain layer

From a technical perspective the app uses:
* Compose for the UI components, screens and theming
* Kotlin Coroutines and Flows for async operations
* Single-activity structure where [MainActivity](java/com/example/petapp/MainActivity.kt) set's up the Compose app
* AndroidX Navigation Compose is used for handling navigation between screens and the navigation backstack
* Koin is used for dependency injection
* Retrofit and Moshi for the Network operations
* MVVM architecture
* JUnit and Kotlin Coroutines Test for unit testing .

## domain

The domain contains our model classes, data access contracts and re-usable business logic.

## data

The data provides the implementation details for the contracts defined by the domain layer.

## feature

The feature package contains everything related to UI

### App screenshot
<img src="https://github.com/user-attachments/assets/207c4ca8-b9ff-42a2-9962-02d537576280" width="320">

<img src="https://github.com/user-attachments/assets/1bb07f4e-4063-41eb-b111-24cc44494dd0" width="320">

