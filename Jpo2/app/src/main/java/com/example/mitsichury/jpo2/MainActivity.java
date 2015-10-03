package com.example.mitsichury.jpo2;

import android.app.Activity;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MainActivity extends Activity implements SensorEventListener{
    private static final int TEMPS = 120;
    SensorManager controleurDeSenseurs;
    ZoneDeDessin zoneDeDessin;
    Sensor accelerometre;
    Handler handler;
    int hauteur;
    int largeur;
    TextView minuteur;
    int tempsRestant;
    Button boutonRecommencer;
    Runnable quiGereLeTemps;
    NumberFormat deuxChiffresPourLesSecondes;
    Runnable testIps;

    float xTouchUp;
    float yTouchUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display ecran = getWindowManager().getDefaultDisplay();
        Point taille = new Point();
        // Si la version est inférieur à celle de honeycomb (13)
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2){
            hauteur = ecran.getHeight();
            largeur = ecran.getWidth();
        }
        else{
            ecran.getSize(taille);
            largeur = taille.x;
            hauteur = taille.y;
        }

        minuteur = (TextView)findViewById(R.id.textView);
        zoneDeDessin = (ZoneDeDessin)findViewById(R.id.drawview);
        zoneDeDessin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xTouchUp = event.getX();
                    yTouchUp = event.getY();
                }

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    zoneDeDessin.definirLaPositionDuPoint(zoneDeDessin.recuperePositionXduPoint()+(int)(xTouchUp - event.getX()), zoneDeDessin.recuperePositionYduPoint()+(int)(yTouchUp - event.getY()));
                    verifieQueSortDecran();

                    xTouchUp = event.getX();
                    yTouchUp = event.getY();
                }

                return true;
            }
        });

        boutonRecommencer = (Button)findViewById(R.id.boutonRecommencer);

        boutonRecommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nouveauJeu();
            }
        });

        zoneDeDessin.donnerTailleEcran(largeur, hauteur);

        controleurDeSenseurs = (SensorManager) getSystemService(SENSOR_SERVICE);

        //accelerometre = controleurDeSenseurs.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        handler = new Handler();

        quiGereLeTemps = new Runnable() {
            @Override
            public void run() {
                if(tempsRestant >0){
                    minuteur.setText(convertisseurSecondeEnTexte());
                    tempsRestant = tempsRestant -1;
                    handler.postDelayed(this, 1000);
                }else{
                    faireApparaitreRecommencerAvecScore();
                }
            }
        };

        testIps = new Runnable() {
            @Override
            public void run() {
                zoneDeDessin.dessiner();
                handler.postDelayed(this, 10);
            }
        };
        handler.post(testIps);

        deuxChiffresPourLesSecondes = new DecimalFormat("00");

        nouveauJeu();
    }

    /**
     * Cache le minuteur, affiche le bouton avec le score
     */
    private void faireApparaitreRecommencerAvecScore() {
        enleverLeCarre();
        cacherMinuteur();
        afficherBouton();
        enleverLeCarre();
    }

    /**
     * Enleve la nourriture du serpent
     */
    private void enleverLeCarre() {
        zoneDeDessin.cacherLeCarre();
    }

    private void afficherCarre(){
        zoneDeDessin.afficherLeCarre();
    }

    /**
     * Affiche le bouton recommencer avec le nombre de carré mangé
     */
    private void afficherBouton() {
        String texte = String.valueOf(zoneDeDessin.savoirLenombreDeCarreMange());
        boutonRecommencer.setText(texte);
        boutonRecommencer.setVisibility(View.VISIBLE);
    }

    /**
     * Cache le minuteur
     */
    private void cacherMinuteur() {
        minuteur.setVisibility(View.GONE);
    }

    /**
     * Cache le bouton
     */
    private void cacherBouton(){
        boutonRecommencer.setVisibility(View.GONE);
    }

    /**
     * Convertie le nombre de seconde en m:ss
     * @return
     * Le temps formatté
     */
    private String convertisseurSecondeEnTexte() {
        int minutes = tempsRestant /60;
        int secondes = tempsRestant %60;

        return minutes+":"+deuxChiffresPourLesSecondes.format(secondes);
    }

    /**
     * Initialise le jeu
     */
    private void nouveauJeu(){
        tempsRestant = TEMPS;
        cacherBouton();
        afficherMinuteur();
        afficherCarre();
        zoneDeDessin.viderLaZoneDeDessin();
        handler.post(quiGereLeTemps);
    }

    /**
     * Affiche le menu
     */
    private void afficherMinuteur() {
        minuteur.setVisibility(View.VISIBLE);
    }

    /**
     * Permet au serpent de passeer d'un bord a l'autre
     */
    public void verifieQueSortDecran(){
        if(zoneDeDessin.recuperePositionXduPoint()<0){
            zoneDeDessin.definirLaPositionDuPoint(largeur, zoneDeDessin.recuperePositionYduPoint());}
        if(zoneDeDessin.recuperePositionXduPoint()> largeur){
            zoneDeDessin.definirLaPositionDuPoint(0, zoneDeDessin.recuperePositionYduPoint());}
        if(zoneDeDessin.recuperePositionYduPoint()<0){
            zoneDeDessin.definirLaPositionDuPoint(zoneDeDessin.recuperePositionXduPoint(), hauteur);}
        if(zoneDeDessin.recuperePositionYduPoint()>hauteur){
            zoneDeDessin.definirLaPositionDuPoint(zoneDeDessin.recuperePositionXduPoint(), 0);}
    }

    // On stock l'appel de l'enregistrement pour le senseur
    @Override
    protected void onResume() {
        //controleurDeSenseurs.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       /* float x, y;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = 3*event.values[0];
            y = 3*event.values[1];

            if((Math.abs(x) > 1 || Math.abs(y) > 1)){
                zoneDeDessin.definirLaPositionDuPoint((int) (zoneDeDessin.recuperePositionXduPoint() - x), (int) (zoneDeDessin.recuperePositionYduPoint() + y));
                verifieQueSortDecran();
                //zoneDeDessin.dessiner();
            }
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // On ne s'en sert pas
    }
}
