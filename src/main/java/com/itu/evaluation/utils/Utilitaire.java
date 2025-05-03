package com.itu.evaluation.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Angelo
 */
public class Utilitaire {

    private Utilitaire() {}

    /**
     * tester si une chaine est numérique
     *
     * @param requested chaine de caractère à tester
     * @return vrai si chaine de caractère numérique sinon faux
     * @throws Exception
     */
    public static boolean isNumeric(String strNum) {
        try {
            @SuppressWarnings("unused")
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param indice
     * @param n
     * @return
     */
    public static int genererNouveauNumero(int indice, int n) {
        int k = 1;
        while (k > 0) {
            if (indice < k * n) {
                return k * n + 2;
            }
            k++;
        }
        return 2;
    }

    /**
     * idem separateurMillier
     *
     * @param nombre
     * @return
     */
    public static String separerEnMillier(String nombre) {
        String s = "";
        char[] tab = nombre.toCharArray();
        char t;
        int k = 1;
        for (int i = tab.length - 1; i >= 0; i--) {
            t = tab[i];
            s = String.valueOf(t) + s;
            if (k == 3) {
                s = " " + s;
                k = 1;
            } else {
                k++;
            }
        }
        return s;
    }

    /**
     * Retourne le dernier jour du mois
     *
     * @param daty
     * @return
     * @throws Exception
     */
    public static Date lastDayOfMonth(Date daty) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(daty);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new java.sql.Date(c.getTimeInMillis());
    }

    /**
     *
     * @param date
     * @return
     */
    public static String formatDateRecherche(String date) {
        String result = "";
        if (date.contains("/") & stringDate(date) != null) {
            String[] split = date.split("/");
            result = split[2] + "-" + split[1] + "-" + split[0];
            return result;
        } else {
            return date;
        }
    }

    public static int getMoisEnCoursReel() {
        Calendar a = Calendar.getInstance();
        return a.get(2) + 1;
    }

    /**
     * Retourne la date donnée + 1 an
     *
     * @return
     */
    public static Date getDateAnneeProchaine(Date arg) throws Exception {
        Date result = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(arg);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int year = cal.get(Calendar.YEAR);
            int mois = Utilitaire.getMoisEnCoursReel();
            String date_s = (day - 1) + "/" + mois + "/" + (year + 1);
            result = Utilitaire.string_date("dd/MM/yyyy", date_s);
        } catch (Exception exc) {
            throw exc;
        }
        return result;
    }

    /**
     * Split une chaîne de caractères par une autre
     *
     * @param mot
     * @param sep
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String[] split(String mot, String sep) {
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(mot, sep);
        @SuppressWarnings("rawtypes")
        Vector v = new Vector();
        while (tokenizer.hasMoreTokens()) {
            v.add(tokenizer.nextToken());
        }
        String retour[] = new String[v.size()];
        v.copyInto(retour);
        return retour;
    }

    /**
     * Retourne l'année d'une chaîne au format DD/MM/YYYY
     *
     * @param daty
     * @return
     */
    public static String getAnnee(String daty) {
        // daty.
        // GregorianCalendar eD = new GregorianCalendar();
        // eD.setTime(string_date("dd/MM/yyyy", daty));
        // return String.valueOf(eD.get(1));
        return split(daty, "/")[2];
    }

    /**
     * Retourne l'année en cours
     *
     * @return
     */
    public static int getAneeEnCours() {
        Calendar a = Calendar.getInstance();
        return a.get(1);
    }

    public static String getJour(String daty) {
        // GregorianCalendar eD = new GregorianCalendar();
        // eD.setTime(string_date("dd/MM/yyyy", daty));
        // return completerInt(2, eD.get(5));
        return completerInt(2, split(daty, "/")[0]);
    }

    public static String getMois(String daty) {
        // GregorianCalendar eD = new GregorianCalendar();
        // eD.setTime(string_date("dd/MM/yyyy", daty));
        // return completerInt(2, eD.get(2) + 1);
        return completerInt(2, split(daty, "/")[1]);
    }

    public static Date dateDuJourSql() {
        return string_date("dd/MM/yyyy", dateDuJour());
    }

    public static Date string_date(String patterne, String daty) {
        try {
            if (daty == null || daty.compareTo("") == 0) {
                return null;
            }
            SimpleDateFormat formatter = new SimpleDateFormat(patterne);
            formatter.applyPattern(patterne);
            formatter.setTimeZone(TimeZone.getTimeZone("EUROPE"));
            String annee = getAnnee(daty);
            int anneecours = getAneeEnCours();
            int siecl = anneecours / 100;
            if (annee.toCharArray().length == 2) {
                annee = String.valueOf(siecl) + annee;
            }
            daty = getJour(daty) + "/" + getMois(daty) + "/" + annee;
            Date hiredate = new Date(formatter.parse(daty).getTime());
            Date date1 = hiredate;
            return date1;
        } catch (Exception e) {
            System.out.println("Error string_date wawawawa :" + e.getMessage());
        }
        Date date = dateDuJourSql();
        return date;
    }

    /**
     * Vérifie si la date est un jour ouvrable
     *
     * @param daty
     * @return
     * @throws Exception
     */
    public static boolean estJourOuvrable(Date daty) throws Exception {
        boolean result = false;
        if (Utilitaire.dayOfDate(daty) != 1 && Utilitaire.dayOfDate(daty) != 7) {
            result = true;
        }
        return result;
    }

    /**
     * Différence entre 2 dates en mois
     *
     * @param dateMax
     * @param dateMin
     * @return
     * @throws Exception
     */
    public static int getDiffMoisPrecis(Date dateMax, Date dateMin) throws Exception {
        try {
            int moisdifference = diffMoisDaty(dateMax, dateMin);
            // LocalDate localDateMax = dateMax.toLocalDate();
            // LocalDate localDateMin = dateMin.toLocalDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateMax);
            int dayMax = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(dateMin);
            int dayMin = cal.get(Calendar.DAY_OF_MONTH);
            int jour = dayMax - dayMin;
            if (jour <= 0) {
                // moisdifference--;
            }
            return moisdifference;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Retourne la liste des jours de la semaine
     *
     * @return
     */
    public static String[] listJours() {
        return new String[] { "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche" };
    }

    /**
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static ArrayList<String> getListDateBetween2Date(String date1, String date2) throws Exception {

        ArrayList<String> result = new ArrayList<>();
        java.util.Date fromDate = modifyDate(splitDate(date1));
        java.util.Date toDate = modifyDate(splitDate(date2));

        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        result.add(date1);
        while (cal.getTime().before(toDate)) {
            cal.add(Calendar.DATE, 1);
            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                result.add(new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime()));
            }
        }
        return result;
    }

    /**
     * Transforme une date en chaîne yyyy/mm/dd au format dd-mm-yyyy
     *
     * @param date
     * @return
     */
    public static String splitDate(String date) {
        String[] split = date.split("/");
        return split[2] + "-" + split[1] + "-" + split[0];
    }

    /**
     * Modifie le format d'une date en yyyy-MM-dd
     *
     * @param inputDate
     * @return
     * @throws Exception
     */
    public static java.util.Date modifyDate(String inputDate) throws Exception {
        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        return date;
    }

    /**
     * Différence entre 2 dates
     *
     * @param date1
     * @param date2
     * @param timeUnit
     * @return
     */
    public static long getDateDiff(java.util.Date date1, java.util.Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Convertit un String en java.util.Date
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static java.util.Date castString2Date(String dateString) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = formatter.parse(dateString);
        return date;
    }

    /**
     * Vérifie si une date est valide ou non
     *
     * @param input
     * @param format
     * @return
     */
    public static boolean isValidDate(String input, String format) {
        boolean valid = false;
        try {
            if (!input.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                java.util.Date output = dateFormat.parse(input);
                valid = dateFormat.format(output).equals(input);
            }
        } catch (Exception ex) {
            valid = false;
        }
        return valid;
    }

    /**
     * Retourne l'heure courante
     *
     * @return
     */
    public static String getCurrentHeure() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String ret = sdf.format(cal.getTime()).toString();
        return ret;
    }

    /**
     * Vérifie si les 2 dates appartiennent au même mois
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean testMemeMois(java.sql.Date d1, java.sql.Date d2) {
        return Utilitaire.getMois(d1) == Utilitaire.getMois(d2) && Utilitaire.getAnnee(d1) == Utilitaire.getAnnee(d2);
    }

    /**
     * Convertit le nombre de jours en HH:MM:SS, dont 1jour = 8h
     *
     * @param jour
     * @return
     */
    public static String convertJour8hEnJourHeureMinute(double jour) {
        String result = "";
        if (jour > 0) {
            int j_part_ent = (int) jour;
            result = j_part_ent + "j";
            double j_part_dec = jour - j_part_ent;
            double heure = j_part_dec * 8;
            int h_part_ent = (int) heure;
            result = result + " " + h_part_ent + "h";
            double h_part_dec = heure - h_part_ent;
            double minute = h_part_dec * 60;
            int m_part_ent = (int) minute;
            result = result + " " + m_part_ent + "min";
        }
        return result;
    }

    public static int getAnnee(Date daty) {
        GregorianCalendar eD = new GregorianCalendar();
        eD.setTime(daty);
        return eD.get(1);
    }

    /**
     * Calculer l'âge par rapport à la date de naissance et une date de repère
     *
     * @param naissance
     * @param dateRepere
     * @return
     */

    @SuppressWarnings("deprecation")
    public static int calculeAgeDate(java.sql.Date naissance, java.sql.Date dateRepere) {
        int age = getAneeEnCours() - getAnnee(naissance);
        @SuppressWarnings({})
        java.sql.Date temp = new java.sql.Date(naissance.getYear(), naissance.getMonth(), naissance.getDay());
        temp.setYear(getAneeEnCours() - 1900);
        if (dateRepere.compareTo(temp) < 0) {
            age--;
        }
        return age;
    }

    /**
     * Convertit une classe de type java.sql.Date en java.util.Date
     *
     * @param sqlDate
     * @return
     */
    public static java.util.Date convertFromSQLDateToUtilDate(java.sql.Date sqlDate) {
        java.util.Date javaDate = null;
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return javaDate;
    }

    /**
     * Retourne le dernier jour du mois par rapport à une date au format
     * yyyy-MM-dd
     *
     * @param daty
     * @return
     */
    public static String getLastDayOfDate(Date daty) {
        String ret = "";
        try {
            java.util.Date dt = convertFromSQLDateToUtilDate(daty);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            java.util.Date lastDayOfMonth = calendar.getTime();
            java.text.DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            return sdf.format(lastDayOfMonth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public static java.util.Date stringToDate(String pattern, String daty) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            java.util.Date hiredate = formatter.parse(daty);
            java.util.Date date1 = hiredate;
            return date1;
        } catch (Exception e) {
            System.out.println("Error stringTodate :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        java.util.Date date = null;
        return date;
    }

    public static String getLastDayOfDate(String daty) {
        String ret = "";
        try {
            Date dt = stringDate(daty);
            ret = getLastDayOfDate(dt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Calcul hormis jour fériés
     *
     * @param mois
     * @param annee
     * @return
     */
    public static int calculNbreJourOuvrable(String mois, String annee) {

        @SuppressWarnings("deprecation")
        java.util.Date startDate = new java.util.Date(Integer.valueOf(annee) - 1900, Integer.valueOf(mois) - 1, 1);
        String dates = Utilitaire.getLastDayOfDate("01/" + mois + "/" + annee);
        java.util.Date endDate = Utilitaire.stringToDate("yyyy-MM-dd", dates);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                workDays++;
            }
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());

        return workDays;
    }

    /**
     * idem getTrimestre (ity no fohy indrindra)
     *
     * @param mois
     * @return
     */
    public static int getTrimestreByMois(int mois) {
        if (mois <= 3) {
            return 1;
        } else if (mois > 3 && mois <= 6) {
            return 2;
        } else if (mois > 6 && mois <= 9) {
            return 3;
        } else if (mois > 9 && mois <= 12) {
            return 4;
        }
        return 0;
    }

    /**
     * Calculer l'âge selon la date donnée
     *
     * @param naissance
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int calculeAge(java.sql.Date naissance) {
        int age = getAneeEnCours() - getAnnee(naissance);
        java.sql.Date temp = new java.sql.Date(naissance.getYear(), naissance.getMonth(), naissance.getDay());
        temp.setYear(getAneeEnCours() - 1900);
        if (dateDuJourSql().compareTo(temp) < 0) {
            age--;
        }
        return age;
    }

    public static int calculeAge(String date) {
        return calculeAge(stringDate(date));
    }

    /**
     *
     * @param periode
     * @param diff
     * @return
     * @throws Exception
     */
    public static int ajoutMoisPeriode(int periode, int diff) throws Exception {
        String daty = "01/" + getMoisPeriode(String.valueOf(periode)) + "/" + getAnneePeriode(String.valueOf(periode));
        java.sql.Date datySql = stringDate(daty);
        java.sql.Date dt = ajoutMoisDate(datySql, diff);
        String annee = getAnnee(datetostring(dt));
        String mois = getMois(datetostring(dt));
        int retour = stringToInt(annee + mois);
        return retour;
    }

    /**
     *
     * @param periode
     * @param diff
     * @return
     * @throws Exception
     */
    public static String ajoutMoisPeriode(String periode, int diff) throws Exception {
        int retour = ajoutMoisPeriode(stringToInt(periode), diff);
        return String.valueOf(retour);
    }

    public static int getMois(Date daty) {
        GregorianCalendar eD = new GregorianCalendar();
        eD.setTime(daty);
        return eD.get(2) + 1;
    }

    /**
     * idem getSemestre
     *
     * @param daty
     * @return
     */
    public static String getPeriode(Date daty) {
        int mois = getMois(daty);
        String periode = "";
        if (mois >= 1 && mois <= 3) {
            periode += getAnnee(daty) + "01";
        }
        if (mois >= 4 && mois <= 6) {
            periode += getAnnee(daty) + "02";
        }
        if (mois >= 7 && mois <= 9) {
            periode += getAnnee(daty) + "03";
        }
        if (mois >= 10 && mois <= 12) {
            periode += getAnnee(daty) + "04";
        }
        return periode;
    }

    /**
     * Retourne la différence entre 2 dates en année
     *
     * @param first
     * @param last
     * @return
     */
    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH)
                || (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static String getAnneePeriode(String periode) throws Exception {
        if (periode.length() != 6) {
            throw new Exception("Format periode invalide");
        }
        return periode.substring(0, 4);
    }

    public static String getMoisPeriode(String periode) throws Exception {
        if (periode.length() != 6) {
            throw new Exception("Format periode invalide");
        }
        return periode.substring(4, 6);
    }

    /**
     * Retourne la date minimale entre d1 et d2
     *
     * @param d1
     * @param d2
     * @return
     */
    public static java.sql.Date dateMin(java.sql.Date d1, java.sql.Date d2) {
        if (d1 == null && d2 == null) {
            return null;
        }
        if (d1 == null) {
            return d2;
        }
        if (d2 == null) {
            return d1;
        }
        return (d1.before(d2)) ? d1 : d2;
    }

    /**
     *
     * @param d
     * @return
     */
    public static String datetostring(java.sql.Date d) {
        String daty = null;
        SimpleDateFormat dateJava = new SimpleDateFormat("dd/MM/yyyy");
        daty = dateJava.format(d);
        return daty;
    }

    /**
     *
     * @param d
     * @return
     */
    public static String datetostring(java.util.Date d) {
        String daty = null;
        SimpleDateFormat dateJava = new SimpleDateFormat("dd/MM/yyyy");
        daty = dateJava.format(d);
        return daty;
    }

    public static String dateMax(String d1, String d2) {
        java.sql.Date retour = dateMax(stringDate(d1), stringDate(d2));
        return datetostring(retour);
    }

    public static String dateMin(String d1, String d2) {
        java.sql.Date retour = dateMin(stringDate(d1), stringDate(d2));
        return datetostring(retour);
    }

    /**
     * Retourne la date maximale entre d1 et d2
     *
     * @param d1
     * @param d2
     * @return
     */
    public static java.sql.Date dateMax(java.sql.Date d1, java.sql.Date d2) {
        if (d1 == null && d2 == null) {
            return null;
        }
        if (d1 == null) {
            return d2;
        }
        if (d2 == null) {
            return d1;
        }
        return (d1.after(d2)) ? d1 : d2;
    }

    /**
     * Le début de la chaîne de caractères sera en majuscule
     *
     * @param autre
     * @return
     */
    public static String convertDebutMajuscule(String autre) {
        char[] c = autre.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

    /**
     * Retourne une date en toutes lettes (ex: 12 Avril 2021)
     *
     * @param dat
     * @return
     */
    public static String datedujourlettre(String dat) {
        String jour = getJour(dat);
        String mois = convertDebutMajuscule(nbToMois(Utilitaire.stringToInt(Utilitaire.getMois(dat))));
        String annee = getAnnee(dat);
        String daty = jour + " " + mois + " " + annee;
        return daty;
    }

    /**
     * Retourne le mois correspondant au nombre; 1 pour Janvier, ...
     *
     * @param nombre
     * @return
     */
    public static String nbToMois(int nombre) {
        String mois = null;
        switch (nombre) {
            case 1: // '\001'
                mois = "janvier";
                break;

            case 2: // '\002'
                mois = "fevrier";
                break;

            case 3: // '\003'
                mois = "mars";
                break;

            case 4: // '\004'
                mois = "avril";
                break;

            case 5: // '\005'
                mois = "mai";
                break;

            case 6: // '\006'
                mois = "juin";
                break;

            case 7: // '\007'
                mois = "juillet";
                break;

            case 8: // '\b'
                mois = "aoï¿½t";
                break;

            case 9: // '\t'
                mois = "septembre";
                break;

            case 10: // '\n'
                mois = "octobre";
                break;

            case 11: // '\013'
                mois = "novembre";
                break;

            case 12: // '\f'
                mois = "decembre";
                break;

            default:
                mois = null;
                break;
        }
        return mois;
    }

    public static int[] transformerMoisAnnee(int mois, int annee) {
        int[] retour = new int[2];
        retour[1] = annee + mois / 12;
        retour[0] = mois % 12;
        if (retour[0] == 0) {
            retour[0] = 12;
            retour[1] = retour[1] - 1;
        }
        return retour;
    }

    /**
     * Retourne la date du jour au format dd/MM/yyyy
     *
     * @return
     */
    public static String dateDuJour() {
        Calendar a = Calendar.getInstance();
        String retour = null;
        retour = String.valueOf(String.valueOf(completerInt(2, a.get(5)))).concat("/");
        retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                .append(completerInt(2, a.get(2) + 1)).append("/")));
        retour = String.valueOf(retour) + String.valueOf(completerInt(4, a.get(1)));
        return retour;
    }

    /**
     * Retourne la valeur de l'heure actuelle en chaîne de caractères HH:MM
     *
     * @return
     */
    public static String heureCouranteHM() {
        Calendar a = Calendar.getInstance();
        String retour = null;
        retour = String.valueOf(String.valueOf(completerInt(2, a.get(11)))).concat(":");
        retour = String.valueOf(String.valueOf(
                (new StringBuffer(String.valueOf(String.valueOf(retour)))).append(completerInt(2, a.get(12)))));
        return retour;
    }

    /**
     * Retourne la valeur de l'heure actuelle en chaîne de caractères
     * HH:MM:SS:MS
     *
     * @return
     */
    public static String heureCourante() {
        Calendar a = Calendar.getInstance();
        String retour = null;
        retour = String.valueOf(String.valueOf(completerInt(2, a.get(11) + 1))).concat(":");
        retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                .append(completerInt(2, a.get(12))).append(":")));
        retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                .append(completerInt(2, a.get(13))).append(":")));
        retour = String.valueOf(retour) + String.valueOf(completerInt(2, a.get(14) / 10));
        return retour;
    }

    /**
     * Compléter un entier (ex: 67, 4 devient 0067)
     *
     * @param longuerChaine
     * @param nombre
     * @return
     */
    public static String completerInt(int longuerChaine, int nombre) {
        String zero = null;
        zero = "";
        for (int i = 0; i < longuerChaine - String.valueOf(nombre).length(); i++) {
            zero = String.valueOf(String.valueOf(zero)).concat("0");
        }

        return String.valueOf(zero) + String.valueOf(String.valueOf(nombre));
    }

    public static String completerInt(int longuerChaine, String nombre2) {
        int nombre = stringToInt(nombre2);
        return completerInt(longuerChaine, nombre);
    }

    /**
     * Convertit une date en chaîne dd/MM/yyyy en java.sql.Date
     *
     * @param daty
     * @return
     */
    public static java.sql.Date stringDate(String daty) {
        if (daty == null || daty.isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date utilDate = sdf.parse(daty);
            return new java.sql.Date(utilDate.getTime());
        } catch (Exception e) {
            System.out.println("Error in stringDate: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retourne le nombre de jours dans un mois d'une année, par rapport à la
     * date donnée
     *
     * @param daty
     * @return
     */
    public static int getNombreJourMois(String daty) {
        try {
            String mois = getMois(daty);
            String an = getAnnee(daty);
            int j = getNombreJourMois(mois, an);
            return j;
        } catch (Exception e) {
            System.out.println("getNombreJourMois : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        int i = 0;
        return i;
    }

    /**
     * Retourne le nombre de jour d'un mois d'une année
     *
     * @param mois
     * @param an
     * @return
     */
    public static int getNombreJourMois(String mois, String an) {
        try {
            String datyInf = getBorneDatyMoisAnnee(mois, an)[0];
            String datySup = getBorneDatyMoisAnnee(mois, an)[1];
            int j = diffJourDaty(datySup, datyInf);
            return j;
        } catch (Exception e) {
            System.out.println("getNombreJourMois : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        int i = 0;
        return i;
    }

    public static Date ajoutJourDate(Date aDate, int nbDay) {
        try {
            Date date = string_date("dd/MM/yyyy", ajoutJourDateString(aDate, nbDay));
            return date;
        } catch (Exception e) {
            System.out.println("Error ajoutJourDate :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        Date date1 = null;
        return date1;
    }

    public static Date ajoutMoisDate(Date aDate, int nbMois) {
        try {
            Date date = string_date("dd/MM/yyyy", ajoutMoisDateString(aDate, nbMois));
            return date;
        } catch (Exception e) {
            System.out.println("Error ajoutMoisDate :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        Date date1 = null;
        return date1;
    }

    public static Date ajoutJourDate(String daty, int jour) {
        try {
            Date date = ajoutJourDate(string_date("dd/MM/yyyy", daty), jour);
            return date;
        } catch (Exception e) {
            System.out.println("Error ajoutJourDate :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        Date date1 = null;
        return date1;
    }

    /**
     * Ajoute le nombre de jours à la date donnée
     *
     * @param aDatee
     * @param nbDay
     * @return
     */
    public static String ajoutJourDateString(Date aDatee, int nbDay) {
        try {
            GregorianCalendar eD = new GregorianCalendar();
            Date aDate = string_date("dd/MM/yyyy", formatterDaty(aDatee));
            eD.setTime(aDate);
            int offset = 1;
            @SuppressWarnings("unused")
            int offsetSunday = 1;
            @SuppressWarnings("unused")
            int offsetSaturday = 2;
            if (nbDay < 0) {
                offset = -1;
                offsetSunday = -2;
                offsetSaturday = -1;
            }
            for (int i = 1; i <= Math.abs(nbDay); i++) {
                eD.add(5, offset);
            }

            String retour = null;
            retour = String.valueOf(String.valueOf(completerInt(2, eD.get(5)))).concat("/");
            retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                    .append(completerInt(2, eD.get(2) + 1)).append("/")));
            retour = String.valueOf(retour) + String.valueOf(completerInt(4, eD.get(1)));
            String s1 = retour;
            return s1;
        } catch (Exception e) {
            System.out.println("Error ajoutJourDateString :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        String s = null;
        return s;
    }

    /**
     * Vérifie si la date donnée est la fin du mois
     *
     * @param aDatee
     * @return
     */
    public static boolean testFinDuMois(Date aDatee) {
        GregorianCalendar eD = new GregorianCalendar();
        Date aDate = string_date("dd/MM/yyyy", formatterDaty(aDatee));
        eD.setTime(aDate);
        GregorianCalendar eD2 = new GregorianCalendar();
        eD2.setTime(eD.getTime());
        eD2.add(5, 1);
        return eD.get(2) != eD2.get(2);
    }

    /**
     * Ajouter des mois à la date donnée
     *
     * @param aDatee
     * @param nbMois
     * @return
     */
    @SuppressWarnings("unused")
    public static String ajoutMoisDateString(Date aDatee, int nbMois) {
        try {
            GregorianCalendar eD = new GregorianCalendar();
            GregorianCalendar eD2 = new GregorianCalendar();
            Date aDate = string_date("dd/MM/yyyy", formatterDaty(aDatee));
            eD.setTime(aDate);
            int offset = 1;

            int offsetSunday = 1;
            int offsetSaturday = 2;
            if (nbMois < 0) {
                offset = -1;
                offsetSunday = -2;
                offsetSaturday = -1;
            }
            for (int i = 1; i <= Math.abs(nbMois); i++) {
                eD.add(2, offset);
            }

            eD2.setTime(eD.getTime());
            if (eD.get(2) == eD2.get(2) && testFinDuMois(aDate)) {
                do {
                    eD2.add(5, 1);
                } while (eD.get(2) == eD2.get(2));
                eD2.add(5, -1);
            }
            String retour = null;
            retour = String.valueOf(String.valueOf(completerInt(2, eD2.get(5)))).concat("/");
            retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                    .append(completerInt(2, eD2.get(2) + 1)).append("/")));
            retour = String.valueOf(retour) + String.valueOf(completerInt(4, eD2.get(1)));
            String s1 = retour;
            return s1;
        } catch (Exception e) {
            System.out.println("Error string_date :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        String s = null;
        return s;
    }

    /**
     * Arrondis un double au 'apr' centième près
     *
     * @param a
     * @param apr
     * @return
     */
    public static double arrondir(double a, int apr) {
        double d;
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setMaximumFractionDigits(apr);
            Number retour = nf.parse(nf.format(a));
            double d1 = retour.doubleValue();
            return d1;
        } catch (Exception e) {
            d = 1.0D;
        }
        return d;
    }

    /*
     * Différence en mois entre 2 dates
     *
     * @param dMaxe
     *
     * @param dMine
     *
     * @return
     */
    public static int diffMoisDaty(Date dMaxe, Date dMine) {
        int result = 0, diffAnnee = 0, yMax = 0, yMin = 0, mMax = 0, mMin = 0;
        GregorianCalendar calMax, calMin;
        if (dMaxe.getTime() < dMine.getTime()) {
            Date temp = dMaxe;
            dMaxe = dMine;
            dMine = temp;
        }
        calMax = new GregorianCalendar();
        calMin = new GregorianCalendar();
        calMin.setTime(dMine);
        calMax.setTime(dMaxe);
        mMin = calMin.get(GregorianCalendar.MONTH);
        mMax = calMax.get(GregorianCalendar.MONTH);
        yMin = calMin.get(GregorianCalendar.YEAR);
        yMax = calMax.get(GregorianCalendar.YEAR);
        diffAnnee = yMax - yMin;
        if (mMax < mMin) {
            diffAnnee--;
            result = 12 - (mMin - mMax);
        } else {
            result = mMax - mMin;
        }
        result += diffAnnee * 12;
        return result;
    }

    public static int diffJourDaty(String dMax, String dMin) {
        return diffJourDaty(string_date("dd/MM/yyyy", dMax), string_date("dd/MM/yyyy", dMin));
    }

    public static int diffMoisDaty(String dMax, String dMin) {
        return diffMoisDaty(string_date("dd/MM/yyyy", dMax), string_date("dd/MM/yyyy", dMin));
    }

    /**
     * Formater une date en local
     *
     * @param daty
     * @return
     */
    public static String formatterDaty(String daty) {
        if ((daty == null) || (daty.compareToIgnoreCase("null") == 0) || (daty.compareToIgnoreCase("") == 0)) {
            return "";
        }
        String valiny = (daty.substring(8, 10) + "/" + (daty.substring(5, 7)) + "/" + (daty.substring(0, 4)));
        return valiny;
    }

    @SuppressWarnings("unused")
    public static String formatterDaty(Date daty) {
        String retour = null;
        return formatterDaty(String.valueOf(daty));
    }

    /**
     * Formate en java.sql.Date en Strinf
     *
     * @param daty
     * @return
     */
    @SuppressWarnings("unused")
    public static String formatterDatySql(java.sql.Date daty) {
        String retour = null;
        return formatterDaty(String.valueOf(daty));
    }

    /**
     * Retourne la différence en jours entre 2 dates
     *
     * @param dMaxe
     * @param dMine
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int diffJourDaty(Date dMaxe, Date dMine) {
        GregorianCalendar eD = new GregorianCalendar();
        GregorianCalendar eD2 = new GregorianCalendar();
        Date dMax = string_date("dd/MM/yyyy", formatterDaty(dMaxe));
        Date dMin = string_date("dd/MM/yyyy", formatterDaty(dMine));
        eD.setTime(dMax);
        eD2.setTime(dMin);
        if (dMaxe.equals(dMine)) {
            return 0;
        }

        BigDecimal result = new BigDecimal(String.valueOf(eD.getTime().getTime() - eD2.getTime().getTime()));
        BigDecimal retour = result.divide(new BigDecimal(String.valueOf(0x5265c00)), 4);
        return 1 + retour.intValue();
    }

    @SuppressWarnings("deprecation")
    public static int diffJourDaty2(Date dMaxe, Date dMine) {
        GregorianCalendar eD = new GregorianCalendar();
        GregorianCalendar eD2 = new GregorianCalendar();
        Date dMax = string_date("dd/MM/yyyy", formatterDaty(dMaxe));
        Date dMin = string_date("dd/MM/yyyy", formatterDaty(dMine));
        eD.setTime(dMax);
        eD2.setTime(dMin);
        if (dMaxe.equals(dMine)) {
            return 1;
        }

        BigDecimal result = new BigDecimal(String.valueOf(eD.getTime().getTime() - eD2.getTime().getTime()));
        BigDecimal retour = result.divide(new BigDecimal(String.valueOf(0x5265c00)), 4);
        return 1 + retour.intValue();
    }

    public static String[] getBorneDatyMoisAnnee(String mois, String an) {
        String retour[] = new String[2];
        GregorianCalendar eD = new GregorianCalendar();
        GregorianCalendar eD2 = new GregorianCalendar();
        String moisF = completerInt(2, mois);
        retour[0] = "01/" + moisF + "/" + an;
        Date daty1 = string_date("dd/MM/yyyy", retour[0]);
        eD.setTime(daty1);
        eD2.setTime(daty1);
        eD2.add(5, 26);
        do {
            eD2.add(5, 1);
        } while (eD.get(2) == eD2.get(2));
        eD2.add(5, -1);
        retour[1] = String.valueOf(String.valueOf(completerInt(2, eD2.get(5)))).concat("/");
        retour[1] = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour[1]))))
                .append(completerInt(2, eD2.get(2) + 1)).append("/")));
        retour[1] = String.valueOf(retour[1]) + String.valueOf(completerInt(4, eD2.get(1)));
        return retour;
    }

    /**
     * Retourne un prix de vente % à la marge de vente donnée
     *
     * @param pu
     * @param marge
     * @return
     */
    public static double getPvente(int pu, int marge) {
        return (double) pu * ((double) 1 + (double) marge / (double) 100);
    }

    /**
     * Etablit la somme de 2 à plusieurs heures: les heures en String doivent
     * être au format HH:MMSS
     *
     * @param heure
     * @return
     */
    public static String sommeHeures(String[] heure) {
        String result = new String();
        int sommeh = 0;
        int sommemn = 0;
        String[] g;
        String hours = "";
        String mn = "";
        for (int i = 0; i < heure.length; i++) {
            g = split(heure[i], ":");
            sommeh = sommeh + stringToInt(g[0]);
            sommemn = sommemn + stringToInt(g[1]);
        }
        int x = sommemn / 60;
        if (x > 0) {
            sommeh = sommeh + x;
            sommemn = sommemn % 60;
        }
        if (sommeh < 10) {
            hours = "0" + sommeh;
        } else {
            hours = "" + sommeh;
        }
        if (sommemn < 10) {
            mn = "0" + sommemn;
        } else {
            mn = "" + sommemn;
        }
        result = "" + hours + ":" + mn + "";
        return result;
    }

    /**
     * Convertit une chaîne de caractères en entier
     *
     * @param s
     * @return
     */
    public static int stringToInt(String s) {
        int j;
        try {
            int result = Integer.parseInt(s);
            return result;
        } catch (NumberFormatException ex) {
            j = 0;
        }
        return j;
    }

    /**
     * Différence entre 2 heures au format HH:MM:SS
     *
     * @param heured
     * @param heuref
     * @return
     */
    public static String diffDeuxheures(String[] heured, String[] heuref) {
        String result = new String();
        int minutes;
        int hours;
        String h;
        String mn;
        if (stringToInt(heured[1]) > stringToInt(heuref[1])) {
            minutes = stringToInt(heured[1]) - stringToInt(heuref[1]);
            hours = stringToInt(heuref[0]) - stringToInt(heured[0]) - 1;
        } else {
            minutes = stringToInt(heuref[1]) - stringToInt(heured[1]);
            hours = stringToInt(heuref[0]) - stringToInt(heured[0]);
        }
        if (hours < 10) {
            h = "0" + hours;
        } else {
            h = "" + hours;
        }
        if (minutes < 10) {
            mn = "0" + minutes;
        } else {
            mn = "" + minutes;
        }
        result = "" + h + ":" + mn + "";
        return result;
    }

    /**
     * Retourne le dernier jour du mois par rapport à une date au format
     * java.sql.Date
     *
     * @param daty
     * @return
     */
    public static java.sql.Date getLastDayOfDateSQL(Date daty) {
        java.sql.Date ret = null;
        try {
            java.util.Date dt = convertFromSQLDateToUtilDate(daty);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            java.util.Date lastDayOfMonth = calendar.getTime();
            ret = new java.sql.Date(lastDayOfMonth.getTime());
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Fonction ayant pour résultat le jour de la semaine correspondant à la
     * date donnée en entier (lundi, mardi, mercredi, ...)
     *
     * @param daty
     * @return
     */
    public static String getJourDate(Date daty) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(daty);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case 1:
                return "Dimanche";
            case 2:
                return "Lundi";
            case 3:
                return "Mardi";
            case 4:
                return "Mercredi";
            case 5:
                return "Jeudi";
            case 6:
                return "Vendredi";
            case 7:
                return "Samedi";
        }
        return null;
    }

    /**
     * Fonction ayant pour résultat le jour de la semaine correspondant à la
     * date donnée en entier (1 pour dimanche, 2 pour lundi, ...)
     *
     * @param daty
     * @return
     */
    public static int dayOfDate(Date daty) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(daty);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        return day;
    }

    /**
     * Retourne le dernier jour du mois (ex: 12/01/2021 va donner comme résultat
     * 31)
     *
     * @param d
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getLastJourInMonth(java.sql.Date d) {
        int year = d.getYear() + 1900;
        int month = d.getMonth();
        int day = d.getDate();
        return getLastJourInMonth(year, month, day);
    }

    /**
     * Retourne le dernier jour d'un mois par rapport au jour et année donnés
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int getLastJourInMonth(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay;
    }

    public static String convertToTimestamp(String inputDateTime) throws Exception {
        if (inputDateTime != null && !inputDateTime.isEmpty()) {
            try {
                inputDateTime = inputDateTime.replace("T", " ");
                if (inputDateTime.length() == 16) {
                    inputDateTime += ":00";
                }
                return inputDateTime;
            } catch (IllegalArgumentException e) {
                throw new Exception("Error: Invalid date format. Please use 'yyyy-MM-dd HH:mm:ss'.", e);
            }
        } else {
            throw new Exception("Error: Date time string is null or empty.");
        }
    }

    /***
     * Convertir une chaine de caractere en double
     *
     * @param string
     * @return
     */
    public static double stringToDouble(String value) {
        double j = 0;
        try {
            Double convert = Double.parseDouble(value);
            return convert;
        } catch (Exception e) {
            j = 0;
        }
        return j;
    }

    public static Date convertHtmlDateTimeLocalToSqlDate(String dateTimeString) {
        try {
            // Définir le format pour parser la chaîne datetime-local
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            // Parser la chaîne en LocalDateTime
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

            // Convertir LocalDateTime en java.sql.Date (en ne gardant que la partie date)
            return Date.valueOf(localDateTime.toLocalDate());
        } catch (DateTimeParseException e) {
            System.err.println("Erreur de parsing de la date : " + e.getMessage());
            return null; // ou vous pouvez choisir de lancer une exception
        }
    }

    public static String convertTocm(String mesures) {
        String valeur = mesures.split(" ")[0];
        String unite = mesures.split(" ")[1];

        double valeurNumerique = Double.parseDouble(valeur);

        String result = "";

        switch (unite) {
            case "m":
                result = String.valueOf(valeurNumerique * 100);
                break;
            case "dm":
                result = String.valueOf(valeurNumerique * 10);
                break;
            default:
                result = valeur;
                break;
        }
        return result;
    }

    /**
     * converti java.sql.Timestamp format date heure en java sql format date
     *
     *
     * @param timestamp
     * @return
     */
    public static java.sql.Date convertTimestampToDate(java.sql.Timestamp timestamp) {
        if (timestamp != null) {
            return new java.sql.Date(timestamp.getTime());
        }
        return null;
    }

    /**
     * Convertit une chaîne de caractères en java.sql.Timestamp.
     *
     * @param dateString La chaîne de caractères représentant une date au format
     *                   "yyyy-MM-dd HH:mm:ss".
     * @return Le java.sql.Timestamp correspondant à la chaîne de caractères, ou
     *         null si une erreur se produit.
     */
    public static Timestamp convertStringToTimestamp(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            java.util.Date parsedDate = dateFormat.parse(dateString);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * extrait l'année d'une java.sql.Date
     *
     */
    public static int extractYearForSQLDate(Date sqlDate) {
        if (sqlDate != null) {
            LocalDate localDate = sqlDate.toLocalDate();
            return localDate.getYear(); // Récupère l'année
        }
        throw new IllegalArgumentException("La date ne doit pas être null");
    }

    /**
     * Convertit un String en java.sql.Date
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date castStringToSqlDate(String dateString) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            java.util.Date utilDate = formatter.parse(dateString);
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            System.err.println("Erreur de format de date: " + e.getMessage());
            return null;
        }
    }

    /**
     * Cette fonction applique la règle de trois pour calculer une valeur manquante
     * dans une proportion donnée : a/b = c/x.
     *
     * @param a la première valeur dans la proportion
     * @param b la deuxième valeur dans la proportion
     * @param c la troisième valeur dans la proportion
     * @return la valeur calculée de x telle que a/b = c/x
     * @throws IllegalArgumentException si a est égal à zéro, car cela entraînerait
     *                                  une division par zéro
     */
    public static double regleDeTrois(double a, double b, double c) {
        if (a == 0) {
            throw new IllegalArgumentException("La valeur de 'a' ne peut pas être zéro.");
        }
        return (b * c) / a;
    }

    public static Timestamp combineDateAndTime(String date, String heure) throws ParseException {
        // Format des chaînes d'entrée
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm:ss");

        // Conversion de la date et de l'heure en objets Date
        java.util.Date parsedDate = inputDateFormat.parse(date);
        java.util.Date parsedTime = inputTimeFormat.parse(heure);

        // Combiner la date et l'heure en une seule chaîne
        String combinedDateTime = new SimpleDateFormat("yyyy-MM-dd").format(parsedDate)
                + " " + new SimpleDateFormat("HH:mm:ss").format(parsedTime);

        // Convertir en Timestamp
        return Timestamp.valueOf(combinedDateTime);
    }
    public static double getConfigurationValue(String key) throws Exception {
        String value = "0";
        String filePath = "/home/emadaly/semestre5/Mdm Baovola/pharmaSys/src/conf.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith(key + "=")) {
                    value = line.split("=")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new Exception("Erreur lors de la lecture du fichier de configuration: " + e.getMessage(), e);
        }

        if (value == null) {
            throw new Exception("Clé de configuration non trouvée: " + key);
        }

        return toDouble(value, "Erreur de cast config");
    }
    public static double toDouble(String value, String errorMessage)throws Exception{
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            throw new Exception(errorMessage+" "+e.getMessage());
        }
    }

    public static String[] splitTextBySeparator(String text, String separator) {
        return Arrays.stream(text.split(separator))
                .map(String::trim)
                .toArray(String[]::new);
    }

}