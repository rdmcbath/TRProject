# TRProject
Uses jsonplaceholder API to demonstrate mapping with Guava, sorting, listview, simple adapter, recyclerview, custom adapter, api consumption with Retrofit2.

Using the following API: http://jsonplaceholder.typicode.com/todos 

This small native mobile app displays the following: 

1. An initial screen that consists of a table with one row for each unique "userId" 
2. In each row there should be a count for the number of incomplete todos (determined by "completed": false) 
3. The rows should be sorted descending by the number of incomplete todos 
4. Clicking on a user will bring up a detail page that contains all the todos associated with that user
