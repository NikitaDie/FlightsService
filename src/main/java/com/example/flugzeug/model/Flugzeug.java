package com.example.flugzeug.model;

import com.example.flugzeug.exception.NotAvailableException;
import com.example.flugzeug.exception.WrongPositionException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Flugzeug
{
    private int plaetzeVordReihen = 4;
    private int plaetzeHintReihen = 6;
    private int vordReihenAnzahl = 3;
    private int hintReihenAnzahl = 4;
    private char[][] sitzplanung;
    private boolean[][] reservierungsplan;
    public Flugzeug()
    {
        InitSitzplanung();
    }

    public Flugzeug(int vordReihenAnzahl, int hintReihenAnzahl)
    {
        checkInput(vordReihenAnzahl, hintReihenAnzahl);
        this.vordReihenAnzahl = vordReihenAnzahl;
        this.hintReihenAnzahl = hintReihenAnzahl;
        InitSitzplanung();
    }

    public Flugzeug(int vordReihenAnzahl, int hintReihenAnzahl, int plaetzeVordReihen, int plaetzeHintReihen)
    {
        checkInput(vordReihenAnzahl, hintReihenAnzahl,
                plaetzeVordReihen, plaetzeHintReihen);
        this.vordReihenAnzahl = vordReihenAnzahl;
        this.hintReihenAnzahl = hintReihenAnzahl;
        this.plaetzeVordReihen = plaetzeVordReihen;
        this.plaetzeHintReihen = plaetzeHintReihen;
        InitSitzplanung();
    }

    public char[][] getSitzplanung()
    {
        return sitzplanung;
    }

    public void InitSitzplanung()
    {
        int langsteReihe = Math.max(plaetzeVordReihen, plaetzeHintReihen);
        sitzplanung = new char[vordReihenAnzahl + hintReihenAnzahl][langsteReihe];
        reservierungsplan = new boolean[vordReihenAnzahl + hintReihenAnzahl][langsteReihe];

        for (int i = 0; i < sitzplanung.length; ++i)
        {
            if (i >= vordReihenAnzahl)
                sitzplanung[i] = getGefuellteReihe(plaetzeHintReihen, langsteReihe);
            else
                sitzplanung[i] = getGefuellteReihe(plaetzeVordReihen, langsteReihe);
        }
    }

    private char[] getGefuellteReihe(int reiheLaenge, int langsteReihe)
    {
        char[] result = new char[langsteReihe];
        int differenz = langsteReihe - reiheLaenge;
        int raumAnfangIndex = langsteReihe / 2 - differenz / 2;
        int raumEndeIndex = raumAnfangIndex + differenz - 1;

        for (int i = 0; i < langsteReihe; ++i)
        {
            if (i < raumAnfangIndex || i > raumEndeIndex)
                result[i] = zahlZuBuchstabe(i);
            else
                result[i] = ' ';
        }

        return result;
    }

    private static char zahlZuBuchstabe(int i)
    {
        final int Alphabetlaenge = 25;
        return (char) ('A' + (i % Alphabetlaenge));
    }

    public static int buchstabeZuZahl(char buchstabe)
    {
        return (buchstabe - 'A');
    }

    private static void checkInput(int... values)
    {
        for (int value : values)
        {
            if (value < 0)
                throw new InvalidParameterException("Die Parameter müssen positiv sein.");
        }
    }

    public void belege(String platz)
    {
        int reihe = platz.charAt(0) - '0' - 1;
        int spalte = buchstabeZuZahl(Character.toUpperCase(platz.charAt(1)));

        try
        {
            if (sitzplanung[reihe][spalte] == ' ')
                throw new WrongPositionException(platz);

            if (reservierungsplan[reihe][spalte])
                throw new NotAvailableException(platz);

            reservierungsplan[reihe][spalte] = true;
        }
        catch (RuntimeException e)
        {
            if (e instanceof NotAvailableException || e instanceof WrongPositionException)
                throw e;

            throw new WrongPositionException(platz);
        }
    }

    public int[] findeReihen(int personen)
    {
        checkInput(personen);

        List<Integer> freieReihen = new ArrayList<>();

        for(int i = 0; i < sitzplanung.length; ++i)
            if (personen <= getAnzahlFreiPlaetze(i))
                freieReihen.add(i);

        int[] result = new int[freieReihen.size()];
        for(int i = 0; i < freieReihen.size(); ++i)
            result[i] = freieReihen.get(i) + 1;

        return result;
    }

    public short getAnzahlFreiPlaetze(int reihe)
    {
        short platzImReiheNacheinander = 0;
        List<Short> plaetzeImReiheNacheinander = new ArrayList<>();

        for (int i = 0; i < sitzplanung[reihe].length; ++i)
        {
            if (reservierungsplan[reihe][i])
            {
                plaetzeImReiheNacheinander.add(platzImReiheNacheinander);
                platzImReiheNacheinander = 0;
                continue;
            }

            if (sitzplanung[reihe][i] != ' ')
                ++platzImReiheNacheinander;
        }
        plaetzeImReiheNacheinander.add(platzImReiheNacheinander);

        return Collections.max(plaetzeImReiheNacheinander);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < sitzplanung.length; ++i)
        {
            for (int j = 0; j < sitzplanung[i].length; ++j)
            {
                char Symbol = reservierungsplan[i][j] ? 'X' : sitzplanung[i][j];
                builder.append(Symbol).append(' ');
            }
            builder.append('\n');
        }

        return builder.toString();
    }
}
