package com.example.booking.bookingapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by l7systems on 1/12/2018.
 */

public class SharedPrefManager {
    public static final String SP_MAHASISWA_APP = "spMahasiswaApp";
    public static final String SP_NAMA = "spNama";
    public static final String SP_EMAIL = "spEmail";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    public static final String SP_IP = "ip";
    public static final String LOGIN_URL = "booking_rooms/forms/android/cek_login_android.php";

    public static final String BOOKING_LIST_URL = "booking_rooms/forms/android/booking_forms_list_android.php";
    public static final String TAMBAH_BOOKING_URL = "booking_rooms/forms/android/booking_forms_save_android.php";
    public static final String HAPUS_BOOKING_URL = "booking_rooms/forms/android/booking_forms_delete_android.php";

    public static final String JAM_URL = "booking_rooms/forms/jam_forms_getdata.php?akses=android";
    public static final String RUANG_URL = "booking_rooms/forms/ruangan_forms_getdata.php?akses=android";


    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_MAHASISWA_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSPIP(String value) {
        if(value.equals("")){
            return "http://"+ sp.getString(SP_IP, "") +"/";
        }
        return "http://"+ sp.getString(SP_IP, "") +"/"+ value;
    }

    public String getLoginUrl() {
        return "http://"+ sp.getString(SP_IP, "")  +"/"+ LOGIN_URL;
    }

    public String getBookingListUrl() {
        return "http://"+ sp.getString(SP_IP, "")  +"/"+ BOOKING_LIST_URL;
    }

    public String getTambahBookingUrl() {
        return "http://"+ sp.getString(SP_IP, "")  +"/"+ TAMBAH_BOOKING_URL;
    }

    public String getHapusBookingUrl() {
        return "http://"+ sp.getString(SP_IP, "")  +"/"+ HAPUS_BOOKING_URL;
    }

    public String getJamUrl() {
        return "http://"+ sp.getString(SP_IP, "")  +"/"+ JAM_URL;
    }

    public String getRuangUrl() {
        return "http://"+ sp.getString(SP_IP, "")  +"/"+ RUANG_URL;
    }

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

}


