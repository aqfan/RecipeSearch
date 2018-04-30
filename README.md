# RecipeSearch

An app that searches for recipes.

Have you ever had a fridge of random ingredients but have no idea what to make with them? Well this app is perfect for you!

This app allows the user to save ingredients, create a shopping list, and save recipes that users find through the search function.

## Table of Contents
1. [Medium Features](#mediumFeatures)
2. [Hard Features](#hardFeatures)
3. [How to Use](#instructions)

## Medium Features <a name="mediumFeatures"></a>
*	Use of RecyclerView with custom adapter
    * My Fridge and Shopping List both use the same custom RecyclerView adapter to the name of items, the amount, and the expiration date. The custom adapter also changes what is shown depending on the fragment the user is in. If the user is in My Fridge, the expiration date and delete button is shown. If the user is in Shopping List, the expiration date is not shown and the checkbox is shown.
* Use of menu items
    * The 3 main menu items are shown on the bottom and they are: Home, Search, and Settings.
    * The Home fragment also has 3 menu items: My Fridge, My Recipes, and Shopping List
* Use of 3+ activities (NOT USED)
    *  I originally had this as a feature but decided to use fragments instead
*	Use of a spinner dropdown
    * In Settings menu, spinner is used to allow user to choose which type of search they would like to do: Search by Recipe Name or Search by Ingredients
    * I originally was going to use the spinner as a dropdown menu but decided that a bottom menu was cleaner
*	Use of SharedPreferences
    * SharedPreferences are used to store the ingredients from My Fridge, the recipes from My Recipes, the shopping list from Shopping List, and settings from Settings locally on the user's phone


## Hard Features <a name="hardFeatures"></a>

*	Use of an HTTP API
    * I used https://spoonacular.com/food-api to search for recipes.
    * Searches recipes using Ingredients and using a regular query to search for recipe name
    * SearchService interface has the methods
*	Use of notifications (NOT USED)
    * I originally wanted to use notifications to remind users of the expiration date of food items but decided to use fragments instead
* Use of fragments
    * Basically everything is a fragment that's linked to the Main Activity


## How to Use <a name="instructions"></a>

* Home
    * My Fridge
        * User can input new item, amount, and expiration date and add it to the list
        * User can also edit items in the list by clicking on the pencil and then clicking the check mark to save
        * User can delete the item by clicking the trash can
        * There's verfications in place to make sure user input is correct and user saves after editing
        * Click on home button to go back to home
    * My Recipes
        * All of the user's stored recipes. Click on the trash can to delete it
        * Click on a recipe to open the recipe's page, then scroll down a little to see a link to the recipe
        * Click on home button to go back to home
    * Shopping List
        * User can input item name and quantity
        * User can also edit items in the list by clicking on the pencil and then clicking the check mark to save
        * User can click the checkbox to cross item off list
        * User can click "Clear Shopping List" button to clear the whole shopping list
* Search
    * If on ingredient search mode, user can search for recipes using ingredients by typing in the ingredients with a space separating each ingredient.
        * Ex: banana chocolate
        * This will get different results than in recipe name search mode
    * If on recipe name search mode, user can search for recipes using recipe names by typing in the recipe name
        * Ex: banana chocolate
        * This will get different results than in ingredient search mode
* Settings
    * User can choose between Ingredient search mode or Recipe Name search mode
    * User can also choose the number of search results that will show up (between 1 and 1000)
    * User must save all changes before seeing the changes in the search fragment
