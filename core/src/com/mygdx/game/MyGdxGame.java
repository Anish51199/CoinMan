package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] man;
    Rectangle manRectangle;
    int manState=0;
    int PauseState=0;
    float gravity=0.2f;
    float velocity=0;
    int manY=0;
    int Score=0;
    BitmapFont font;
    int GameState=0;

    Texture dizzy;

    // ADD COIN
    Random random; //for height
    ArrayList<Integer>CoinX=new ArrayList<>();
    ArrayList<Integer>Coiny=new ArrayList<>();
    Texture coin;
    int CoinCount=0;

    // Create bomb
    ArrayList<Integer>BombX=new ArrayList<>();
    ArrayList<Integer>BombY=new ArrayList<>();
    Texture bomb;
    int BombCount=0;

    //
    ArrayList<Rectangle>CoinRectangle=new ArrayList<Rectangle>();
    ArrayList<Rectangle>BombRectangle=new ArrayList<Rectangle>();
    @Override
    public void create () {
        batch = new SpriteBatch();
        background=new Texture("bg.png");
        man=new Texture[4];
        man[0]=new Texture("frame-1.png");
        man[1]=new Texture("frame-2.png");
        man[2]=new Texture("frame-3.png");
        man[3]=new Texture("frame-4.png");
        manY=Gdx.graphics.getHeight()/2;

        dizzy=new Texture("dizzy-1.png");

        coin=new Texture("coin.png");
        bomb=new Texture("bomb.png");
        random=new Random();

        font=new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
    }
    public void makeCoin(){
        float height=random.nextFloat()*Gdx.graphics.getHeight();
        Coiny.add((int)height);
        CoinX.add(Gdx.graphics.getWidth());
    }

    public void makeBomb(){
        float height=random.nextFloat()*Gdx.graphics.getHeight();
        BombY.add((int)height);
        BombX.add(Gdx.graphics.getWidth());
    }
    @Override
    public void render () {
        batch.begin();

        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        if(GameState==1){
            //ON
            if(CoinCount<100){
                CoinCount++;
            }else{
                CoinCount=0;
                makeCoin();
            }
            CoinRectangle.clear();
            for(int i=0;i<CoinX.size();i++){
                batch.draw(coin,CoinX.get(i),Coiny.get(i));
                CoinX.set(i,CoinX.get(i)-4);
                CoinRectangle.add(new Rectangle(CoinX.get(i),Coiny.get(i),coin.getWidth(),coin.getHeight()));
            }
            // Make Bomb
            if(BombCount<100){
                BombCount++;
            }
            else{
                BombCount=0;
                makeBomb();
            }
            BombRectangle.clear();
            for(int i=0;i<BombX.size();i++){
                batch.draw(bomb,BombX.get(i),BombY.get(i));
                BombX.set(i,BombX.get(i)-8);
                BombRectangle.add(new Rectangle(BombX.get(i),BombY.get(i),bomb.getWidth(),bomb.getHeight()));
            }

            // For man running
            if(PauseState<6){
                PauseState++;
            }
            else{
                PauseState=0;
                if(manState<3){
                    manState++;
                }
                else {
                    manState=0;
                } }

            if(Gdx.input.justTouched()){
                velocity =-10;
            }
        }
        else if(GameState==0){
            if(Gdx.input.justTouched()){
                GameState=1;
            }
        }
        else{
            //OVER
            if(Gdx.input.justTouched()){
                GameState=1;
                manY=Gdx.graphics.getHeight()/2;
                Score=0;
                velocity=0;
                Coiny.clear();
                CoinX.clear();
                CoinRectangle.clear();
                CoinCount=0;
                BombY.clear();
                BombX.clear();
                BombRectangle.clear();
                BombCount=0;
            }
        }
//COIN

        velocity +=gravity;
        manY -=velocity;

        if(manY<=0){
            manY=0;
        }

        if(GameState==2) {
            batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
        }
        else{
            batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
        }
        manRectangle=new Rectangle(Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,manY,man[manState].getWidth(),man[manState].getHeight());
        for(int i=0;i<CoinRectangle.size();i++){
            if(Intersector.overlaps(manRectangle,CoinRectangle.get(i))){
                Score++;
                CoinRectangle.remove(i);
                CoinX.remove(i);
                Coiny.remove(i);
                break;
            }
        }

        font.draw(batch,Integer.toString(Score),100,200);

        for(int i=0;i<BombRectangle.size();i++){
            if(Intersector.overlaps(manRectangle,BombRectangle.get(i))){
             //   Gdx.app.log("Bomb","BOMB COLLISON");
                GameState=2;
            }
        }
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();

    }
}
