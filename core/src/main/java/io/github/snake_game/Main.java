package io.github.snake_game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


import java.util.Random;


/** {@link ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {


    static final int winHeight = 704;
    static final int winWidth = 1024;
    static final int tileSize = 32;

    int lenght = 3;
    int applesEaten = 0;
    int appleX;
    int appleY;
    int[] snakeX = new int[winHeight/tileSize * winWidth/tileSize];
    int[] snakeY  = new int[winHeight/tileSize * winWidth/tileSize];

    float delta;

    char direction = 'R';

    boolean gameRunning = true;
    ShapeRenderer shape;
    Random random;

    @Override
    public void create () {
        shape = new ShapeRenderer();
        random = new Random();
        createApple();
    }

    @Override
    public void render () {
        delta += Gdx.graphics.getDeltaTime();
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);


        shape.begin(ShapeRenderer.ShapeType.Filled);


        createTiles();
        mouseInput();
        if(delta > .1 && gameRunning){
            checkForKeyboard();
            moveSnake();
            checkForCollision();
            if(snakeX[0] == appleX && snakeY[0] == appleY)
                eatApple();
        }



        shape.setColor(Color.RED);
        shape.rect(appleX,appleY,tileSize,tileSize);

        drawSnake();
        shape.end();
    }


    void createTiles(){
        for (int i = 0;i < Gdx.graphics.getHeight() - tileSize;i += tileSize){
            for(int j = 0; j < Gdx.graphics.getWidth() - tileSize;j += tileSize){
                if(j%64 == 0 ^ i%64 == 0)shape.setColor(114/255f, 191/255f, 120/255f,1f);
                else shape.setColor(148/255f, 200/255f, 117/255f,1f);
                shape.rect(j,i,tileSize,tileSize);
            }
        }


    }
    void drawSnake(){
        for(int i = 0;i<lenght;i++){
            shape.setColor(8/255f, 194/255f, 1f,1f);
            shape.rect(snakeX[i],snakeY[i],tileSize,tileSize);
        }
    }

    void moveSnake(){
        for(int i = lenght;i>0;i--){
            snakeX[i] = snakeX[i-1];
            snakeY[i] = snakeY[i-1];
        }

        switch (direction){

            case 'R':
                snakeX[0] +=tileSize;
                break;
            case 'L':
                snakeX[0] -=tileSize;
                break;
            case 'U':
                snakeY[0] +=tileSize;
                break;
            case 'D':
                snakeY[0] -=tileSize;
                break;
        }

        delta =0;
    }

    void checkForKeyboard(){

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && direction != 'D')direction = 'U';
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && direction != 'U')direction = 'D';
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && direction != 'L')direction = 'R';
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && direction != 'R')direction = 'L';


    }

    void createApple(){
        appleX = random.nextInt((Gdx.graphics.getWidth() - tileSize)/tileSize) * tileSize;
        appleY = random.nextInt((Gdx.graphics.getHeight() - tileSize)/tileSize) * tileSize;

    }

    void eatApple(){
        createApple();
        applesEaten +=1;
        lenght +=1;
    }
    void checkForCollision(){
        for(int i = 1;i < lenght;i++){
            if(snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]){
                gameRunning = false;
                break;
            }
        }

        if(snakeX[0] < 0)gameRunning = false;
        if(snakeX[0] > Gdx.graphics.getWidth() - tileSize)gameRunning = false;
        if(snakeY[0] < 0)gameRunning = false;
        if(snakeY[0] > Gdx.graphics.getHeight() - tileSize)gameRunning = false;

    }

    private Vector2 mouseFirst = new Vector2();
    private Vector2 mouseLast = new Vector2();
    private Vector2 dir = new Vector2();
    private boolean wasPressed = false;

    public void mouseInput() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!wasPressed) { // First press
                mouseFirst.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

                wasPressed = true;
            }
        } else if (wasPressed) { // Released
            mouseLast.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            dir.set(mouseLast).sub(mouseFirst);



            wasPressed = false; // Reset for the next input
            calculateDirection(dir); // Perform any desired operation with the direction
        }
    }






    void calculateDirection(Vector2 dir){

        if(Math.abs(dir.x) > Math.abs(dir.y)){
            if(dir.x > 0 && direction != 'L')direction = 'R';
            else if (dir.x < 0 && direction != 'R')direction = 'L';
        }
        else{
            if(dir.y > 0 && direction != 'D')direction = 'U';
            else if(dir.y < 0 && direction != 'U') direction = 'D';

        }

    }
}



