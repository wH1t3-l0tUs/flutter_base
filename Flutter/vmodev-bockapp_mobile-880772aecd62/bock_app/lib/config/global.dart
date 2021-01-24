import 'package:flutter/material.dart';
import 'package:flutterbloc/utils/shared_preferences.dart';
import 'package:google_fonts/google_fonts.dart';

class Global {
  static String emailDefault = "hoang.nguyen@vmodev.com";
  static String passwordDefault = "123456";
  static SpUtil storage;
  static String baseURL = "http://dummy.restapiexample.com/";

  Size getScreenSize(BuildContext context) {
    return MediaQuery.of(context).size;
  }

  static void createGlobalStorage() async {
    Global.storage = await SpUtil.getInstance();
  }
}

class AppTextStyle {
  static TextStyle blackSmallText =
      GoogleFonts.lato(color: Colors.black, fontSize: 12.0);

  static TextStyle blackNormalText =
      GoogleFonts.lato(color: Colors.black, fontSize: 14.0);

  static TextStyle blackLargeText =
      GoogleFonts.lato(color: Colors.black, fontSize: 16.0);

  static TextStyle blackExtraText =
      GoogleFonts.lato(color: Colors.black, fontSize: 20.0);

  static TextStyle blackHugeText =
      GoogleFonts.lato(color: Colors.black, fontSize: 30.0);

  static TextStyle whiteSmallText =
      GoogleFonts.lato(color: Colors.black, fontSize: 12.0);

  static TextStyle whiteNormalText =
      GoogleFonts.lato(color: Colors.white, fontSize: 14.0);

  static TextStyle whiteLargeText =
      GoogleFonts.lato(color: Colors.white, fontSize: 16.0);

  static TextStyle whiteExtraText =
      GoogleFonts.lato(color: Colors.white, fontSize: 20.0);

  static TextStyle whiteHugeText =
      GoogleFonts.lato(color: Colors.white, fontSize: 30.0);
}
