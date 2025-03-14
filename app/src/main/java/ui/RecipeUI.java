package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

import data.RecipeFileHandler;

public class RecipeUI {
    private BufferedReader reader;
    private RecipeFileHandler fileHandler;

    public RecipeUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        fileHandler = new RecipeFileHandler();
    }

    public RecipeUI(BufferedReader reader, RecipeFileHandler fileHandler) {
        this.reader = reader;
        this.fileHandler = fileHandler;
    }

    public void displayMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("Main Menu:");
                System.out.println("1: Display Recipes");
                System.out.println("2: Add New Recipe");
                System.out.println("3: Search Recipe");
                System.out.println("4: Exit Application");
                System.out.print("Please choose an option: ");

                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        // 設問1: 一覧表示機能
                        displayRecipes();
                        break;
                    case "2":
                        // 設問2: 新規登録機能
                        addNewRecipe();
                        break;
                    case "3":
                        // 設問3: 検索機能
                        searchRecipe();
                        break;
                    case "4":
                        System.out.println("Exit the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error reading input from user: " + e.getMessage());
            }
        }
    }

    /**
     * 設問1: 一覧表示機能
     * RecipeFileHandlerから読み込んだレシピデータを整形してコンソールに表示します。
     */
    private void displayRecipes() throws IOException{
        // try {

        // } catch (IOException e) {
        //     System.out.println("Error reading file:" + e.getMessage());
        // }

        try{
            //menuListにrecipeFileHandlerを追加する
            ArrayList<String> menuList = fileHandler.readRecipes();
            
            //読み込んだレシピデータが空の場合に例外処理
            if(menuList.size() == 0){
                throw new Exception("No recipes available.");
            }
            System.out.println("Recipes:");
            System.out.println("-----------------------------------");
            //レシピデータを整形しコンソールに表示
            for(int i = 0; i < menuList.size(); i++){
                String text1 = menuList.get(i);
                String text2 = "";
                String[] menus = text1.split(",");
                for(int j = 0; j < menus.length; j++){
                    //最初はRecipe Name:で表示
                    if(j == 0){
                        text2 +="Recipe Name: " + menus[j] + "\n" +"Main Ingredients:";
                    }else{
                        text2 += menus[j];
                        if(j < menus.length - 1){
                            text2 += ", ";
                        }
                    }
                }
                System.out.println(text2);
                System.out.println("-----------------------------------");
            }
        }catch (IOException e) {
            System.out.println("Error reading file:" + e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * 設問2: 新規登録機能
     * ユーザーからレシピ名と主な材料を入力させ、RecipeFileHandlerを使用してrecipes.txtに新しいレシピを追加します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void addNewRecipe() throws IOException {
        //レシピ名入力
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter recipe name:");
        String inputRecipeName = reader.readLine();

        //主な材料名入力
        System.out.println("Enter main ingredients (comma separated):");
        String inputIngredients = reader.readLine();
        //addRecipeメソッド呼び出し
        fileHandler.addRecipe(inputRecipeName, inputIngredients);

        System.out.print("Recipe added successfully.");
    }

    /**
     * 設問3: 検索機能
     * ユーザーから検索クエリを入力させ、そのクエリに基づいてレシピを検索し、一致するレシピをコンソールに表示します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void searchRecipe() throws IOException {
        //ユーザーから検索クエリを入力する('name=Tomato&ingredient=Garlic')
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter search query (e.g., 'name=Tomato&ingredient=Garlic'):");
        String inputWords =reader.readLine();
        //nameとingredientに分ける
        String[] pairs = inputWords.split("&");
        String name ="";
        String ingredient = "";
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            if(keyValue.length == 2 && keyValue[0].equals("name")){
                //nameを受けとる
                name = keyValue[1];
            }else if(keyValue.length == 2 && keyValue[0].equals("ingredient")){
                //ingredientを受けとる
                ingredient = keyValue[1];
            }
        }

        //recipes.txtからテキストを受ける
        String text2 = "";
        //nameとingredientが含まれていたら出力
        for(String recipeList : fileHandler.readRecipes()){
            //レシピから料理名、主な材料名を分ける
            String dishName = "";
            String ingredientName = "";
            String[] recipe = recipeList.split(",");
            for(int i= 0; i < recipe.length; i++){
                if(i == 0){
                    //料理名を代入
                    dishName = recipe[i];
                }else{
                    //主な材料名を代入
                    ingredientName += recipe[i];
                }
            }
            //含まれているか確認
            if(dishName.contains(name) && ingredientName.contains(ingredient)){
            text2 += recipeList + "\n";
            }
        }
        System.out.println("Search Results:");
        System.out.println(text2);
        if(text2 ==""){
            System.out.println("No recipes found matching the criteria.");
        }
        //
    }

}

