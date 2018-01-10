package com.example.booking.bookingapp;

/**
 * Created by My-PC on 07/01/2018.
 */

public class Config {
    public static final String url = "http://192.168.56.1/";
    public static final String LOGIN_URL = url + "booking_rooms/forms/android/cek_login_android.php";

    public static final String BOOKING_LIST_URL = url + "booking_rooms/forms/android/booking_forms_list_android.php";
    public static final String TAMBAH_BOOKING_URL = url + "booking_rooms/forms/android/booking_forms_save_android.php";

    public static final String JAM_URL = url + "booking_rooms/forms/jam_forms_getdata.php?akses=android";
    public static final String RUANG_URL = url + "booking_rooms/forms/ruangan_forms_getdata.php?akses=android";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
}
