package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Vector;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.util.Log;
import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


/**
 * YAPILACAKLAR & KOD İNCELEMESİ

 * 1- **Ekranda göster draw methodunu göster ki hatan var ise geri dön.**
 * 2- Resimleri kırpmak için tilesrc, yapıştırmak için tiledst fakat biz draw methodunun içinde
 * for loopu ile yazdırdık
 *
 * 3- Kareno ile tanımlanan variableımızda aslında hiçbirşey değiştirmedik, yanlızca kovboyu
 * yürütmek amaçlı heryerde kullanacbiliceğimiz bir variablımız oldu
 *
 * 4- **Kolaydan Zora doğru yol al. (hep)**
 * 5- Touch methodlarına indik. Orası telefon ekranına dokunup çektiğimiz tarafa doğru
 * kovboyu yöneltiyor.
 *
 * 6- Bulletoffset başlangıç yerini belirlemek için kullandık.
 * 7- animasyonno - 1 veya 0 olabilir ve o sırayı alıp almamaya yarar.
 * 8- animasyonyonu - karakterin hangi yöne gideceğini gösteren satırı alır.
 * 9- foreach metoduna bak olayları kısaltıyor.
 * 10- "temp" olanlar geçici variablelardır. Onlar önceki denemeler için deneme amaçlı kullanılmıştır.
 *
 */



public class GameCanvas extends BaseCanvas {

    /*Global Değişkenler*/
    private Bitmap tileset, spritesheet, bullet;
    private int kareno, animasyonno, animasyonyonu;
    private int spritex, spritey , hiz, hizx, hizy, bulletoffsetx_temp, bulletoffsety_temp, bulletspeed; // Shift+F6 - Refactor Kısatuşu
    private int bulletx_temp, bullety_temp;


    public Vector <Rect> bulletdst2;
    public Vector <Integer> bulletx2, bullety2, bulletoffsetx2, bulletoffsety2, bulletspeedx2, bulletspeedy2;
    private Rect tilesrc, tiledst, spritesrc, spritedst, bulletsrc;



    /*İnteraktif Değişkenler*/
    int touchx, touchy; // Ekrana nereye bastığımızın koordinatlarını tutar


    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {
        tileset = Utils.loadImage(root, "images/tilea2.png");
        tilesrc = new Rect();
        tiledst = new Rect();

        spritesheet = Utils.loadImage(root, "images/cowboy.png");
        spritesrc = new Rect();
        spritedst = new Rect();

        kareno = 0;
        animasyonno = 1;
        animasyonyonu = 0;

        bullet = Utils.loadImage(root, "images/bullet.png");
        bulletsrc = new Rect();
        //bulletdst = new Rect();


        spritex = 0;
        spritey = 0;

        bulletoffsetx_temp = 256;
        bulletoffsety_temp = 128;

        bulletspeed = 32;
        //bulletspeedx=0;
        //bulletspeedy=0;

        bulletx_temp = 0;
        bullety_temp = 0;


        bulletdst2 = new Vector<>();
        bulletx2 = new Vector<>();
        bullety2 = new Vector<>();
        bulletoffsetx2 = new Vector<>();
        bulletoffsety2 = new Vector<>();
        bulletspeedx2 = new Vector<>();
        bulletspeedy2 = new Vector<>();

        hiz = 16;
        hizx = 0;
        hizy = 0;


    }

    public void update() {


        tilesrc.set(0,0,64,64);

        spritex += hizx;
        spritey += hizy;

        for ( int i=0; i<bulletx2.size(); i++){

            bulletx2.set(i, bulletx2.elementAt(i)+bulletspeedx2.elementAt(i));
            bullety2.set(i, bullety2.elementAt(i)+bulletspeedy2.elementAt(i));

            if(bulletx2.elementAt(i) > getWidth() || bulletx2.elementAt(i) < 0 || bullety2.elementAt(i) > getHeight() || bullety2.elementAt(i) < 0){
                bulletx2.removeElementAt(i);
                bullety2.removeElementAt(i);
                /*bulletoffsetx2.removeElementAt(i);
                bulletoffsety2.removeElementAt(i);*/
                bulletdst2.removeElementAt(i);
                bulletspeedx2.removeElementAt(i);
                bulletspeedy2.removeElementAt(i);
            }
            //Log.i("Control", String.valueOf(bulletx2.size()));  //BU KISIMDA MERMİLER EKRANDAN ÇIKTIĞINDA SİLİNİYOR MU KONTROL EDİYORUZ.
        }
/*
        bulletx += bulletspeedx;
        bullety_temp += bulletspeedy;
*/
        if(spritex+256 > getWidth() || spritex < 0){
            hizx = 0;
            /*animasyonno = 0; // Eğer ekranın sonuna ulaşırsa, animasyonno 0'a eşitleyerek aşağıdaki
            kondisyonu kareno artırmasını 0'a eşitledik
        */
        }
        if(spritey+256 > getHeight() || spritey < 0){
            hizy= 0;
            // animasyonno=0;
        }

        if(animasyonno==1){
            kareno++; //Kareyi arttırmak için
        }
        else if(animasyonno==0){
            kareno = 0;
        }

        if(kareno>8){
            kareno=1;
        }

        if(hizx > 0){
            animasyonyonu = 0;
        }
        else if(hizy > 0){
            animasyonyonu = 9;
        }

        if(Math.abs(hizx) > 0 || Math.abs(hizy) > 0){
            animasyonno = 1;
        }
        else{
            animasyonno = 0;
        }


        spritesrc.set(kareno*128, animasyonyonu*128, (kareno+1)*128, (animasyonyonu+1)*128);        //Resimden aldığımız koordinatlar
        spritedst.set(spritex, spritey, spritex+256, spritey+256);       //Ekrana çizeleceği koordinatlar


        bulletsrc.set(0,0,70,70);

        for(int i= 0; i<bulletdst2.size(); i++){

            bulletdst2.elementAt(i).set(bulletx2.elementAt(i), bullety2.elementAt(i), bulletx2.elementAt(i) + 32, bullety2.elementAt(i) + 32);


        }



    }

    public void draw(Canvas canvas) {



        for (int i=0;i<getWidth();i+=128){
            for(int j=0;j<getHeight();j+=128){
                tiledst.set(i, j, i+128, j+128);
                canvas.drawBitmap(tileset,tilesrc,tiledst,null);
            }
        }

        canvas.drawBitmap(spritesheet, spritesrc, spritedst, null);

        //canvas.drawBitmap(bullet, bulletsrc, bulletdst, null);

        for(int i= 0; i<bulletdst2.size(); i++){

            canvas.drawBitmap(bullet, bulletsrc, bulletdst2.elementAt(i), null);

        }

        //bulletoffsetx+=bulletspeedx;
    }

    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
    }

    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void touchDown(int x, int y) {
        touchx=x;
        touchy=y;
    /*
        bulletspeed = 32;
        bulletspeedx =bulletspeed;
     */
    }

    public void touchMove(int x, int y) {
    }

    public void touchUp(int x, int y) {
        if(x-touchx>100){
            animasyonno=1;
            animasyonyonu=0;
            hizx=hiz;
            hizy=0;
        }
        else if(touchx-x>100){
            animasyonno=1;
            animasyonyonu=1;
            hizx=-hiz;

            hizy=0;
        }
        else if(y-touchy>100){
            animasyonno=1;
            animasyonyonu=9;
            hizy=hiz;

            hizx=0;
        }
        else if(touchy-y>100){
            animasyonno=1;
            animasyonyonu=5;
            hizy=-hiz;
            hizx=0;
        }
        else{
            animasyonno = 0;
            hizx = 0;
            hizy = 0;
            bulletspeed = 32;


            if(animasyonyonu == 0){
                bulletspeedx2.add(bulletspeed);
                bulletspeedy2.add(0);
                bulletoffsetx_temp= 256;
                bulletoffsety_temp = 128;
            }
            else if(animasyonyonu == 1){
                bulletspeedx2.add(-bulletspeed);
                bulletspeedy2.add(0);
                bulletoffsetx_temp=0;
                bulletoffsety_temp =128;
            }
            else if(animasyonyonu == 9){
                bulletspeedy2.add(bulletspeed);
                bulletspeedx2.add(0);
                bulletoffsetx_temp= 128;
                bulletoffsety_temp = 256;
            }
            else if(animasyonyonu == 5){
                bulletspeedy2.add(-bulletspeed);
                bulletspeedx2.add(0);
                bulletoffsetx_temp= 128;
                bulletoffsety_temp = 0;
            }
            bulletx2.add(spritex+bulletoffsetx_temp);
            bullety2.add(spritey+ bulletoffsety_temp);

            bulletx_temp = spritex + bulletoffsetx_temp;
            bullety_temp = spritey + bulletoffsety_temp;

            bulletdst2.add(new Rect(bulletx_temp, bullety_temp, bulletx_temp +32, bullety_temp + 32));
        }
    }


    public void pause() {

    }


    public void resume() {

    }


    public void reloadTextures() {

    }


    public void showNotify() {
    }

    public void hideNotify() {
    }

}
