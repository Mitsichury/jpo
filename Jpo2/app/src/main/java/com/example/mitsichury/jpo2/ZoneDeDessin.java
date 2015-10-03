package com.example.mitsichury.jpo2;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by MITSICHURY on 22/09/2015.
 */
public class ZoneDeDessin extends View{

    private ArrayList<PointDessin> points;
    private Carre carre;
    private Random r;
    private int xRand;
    private int yRand;

    private int tailleXecran;
    private int tailleYecran;


    public ZoneDeDessin(Context context, AttributeSet attrs) {
        super(context, attrs);
        points = new ArrayList<>();// new Point(100,100);
        points.add(new PointDessin(100, 100, Color.RED));
        carre = new Carre(100,100, 10, Color.RED);
        r = new Random();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint pinceau = new Paint() ; // Le pinceau

        for(PointDessin point : points) {
            pinceau.setColor(point.getColor());
            canvas.drawCircle(point.getX(), point.getY(), 10, pinceau);
        }
        canvas.drawRect((float) carre.getX(), (float) carre.getY(), (float) (carre.getX() + carre.getTaille()), (float) (carre.getY() + carre.getTaille()), pinceau);
    }

    public void definirLaPositionDuPoint(int x, int y){
        PointDessin pointTmp = points.get(0);
        for(int i=points.size()-1; i>0; i--){
            points.get(i).setPosition(points.get(i-1).getX(), points.get(i-1).getY());
        }

        points.get(0).setPosition(x, y);

        if(pointTmp.getX()>=carre.getX() &&
                pointTmp.getX()<=carre.getX()+carre.getTaille()){
            if(pointTmp.getY()>=carre.getY() &&
                    pointTmp.getY()<=carre.getY()+carre.getTaille()){
                points.add(new PointDessin(points.get(points.size() - 1).getX(), points.get(points.size()-1).getY(), carre.getColor()));
                changeLeCarreDePosition();
            }
        }
        //this.invalidate();
    }

    private void changeLeCarreDePosition() {
        xRand = r.nextInt(tailleXecran-carre.getTaille())+carre.getTaille();
        yRand = r.nextInt(tailleYecran-carre.getTaille())+carre.getTaille();
        int couleurTemporaire = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
        carre.setPosition(xRand, yRand);
        carre.setColor(couleurTemporaire);
    }

    public void dessiner(){this.invalidate();}
    public int recuperePositionXduPoint(){
        return points.get(0).getX();
    }

    public int recuperePositionYduPoint(){return points.get(0).getY();}

    public void donnerTailleEcran(int x, int y) {
        this.tailleYecran = y;
        this.tailleXecran = x;
    }

    public void viderLaZoneDeDessin(){
        points.clear();
        points.add(new PointDessin(100,100, Color.RED));
    }

    public void cacherLeCarre(){
        carre.setPosition(10000, 10000);
        //this.invalidate();
    }

    public void afficherLeCarre(){
        changeLeCarreDePosition();
        //this.invalidate();
    }

    public int savoirLenombreDeCarreMange(){
        return points.size();
    }
}

