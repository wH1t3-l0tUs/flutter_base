package io.driverdoc.testapp.ui.utils

import android.content.Context
import android.util.TypedValue
import android.widget.EditText
import io.driverdoc.testapp.R
import io.driverdoc.testapp.data.model.CCPCountry
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object StringUtils {
    val TIME_FORMAT="dd/MM/yyyy HH:mm:ss"
    val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    fun isBlank(content: String?) = content == null || content.equals("")
    internal var DEFAULT_FLAG_RES = -99

    val code = "{\"code\":[{'name':\"Israel\",\"dial_code\":\"+972\",\"code\":\"IL\"},{\"name\":\"Afghanistan\",\"dial_code\":\"+93\",\"code\":\"AF\"},{\"name\":\"Albania\",\"dial_code\":\"+355\",\"code\":\"AL\"},{\"name\":\"Algeria\",\"dial_code\":\"+213\",\"code\":\"DZ\"},{\"name\":\"AmericanSamoa\",\"dial_code\":\"+1 684\",\"code\":\"AS\"},{\"name\":\"Andorra\",\"dial_code\":\"+376\",\"code\":\"AD\"},{\"name\":\"Angola\",\"dial_code\":\"+244\",\"code\":\"AO\"},{\"name\":\"Anguilla\",\"dial_code\":\"+1 264\",\"code\":\"AI\"},{\"name\":\"Antigua and Barbuda\",\"dial_code\":\"+1268\",\"code\":\"AG\"},{\"name\":\"Argentina\",\"dial_code\":\"+54\",\"code\":\"AR\"},{\"name\":\"Armenia\",\"dial_code\":\"+374\",\"code\":\"AM\"},{\"name\":\"Aruba\",\"dial_code\":\"+297\",\"code\":\"AW\"},{\"name\":\"Australia\",\"dial_code\":\"+61\",\"code\":\"AU\"},{\"name\":\"Austria\",\"dial_code\":\"+43\",\"code\":\"AT\"},{\"name\":\"Azerbaijan\",\"dial_code\":\"+994\",\"code\":\"AZ\"},{\"name\":\"Bahamas\",\"dial_code\":\"+1 242\",\"code\":\"BS\"},{\"name\":\"Bahrain\",\"dial_code\":\"+973\",\"code\":\"BH\"},{\"name\":\"Bangladesh\",\"dial_code\":\"+880\",\"code\":\"BD\"},{\"name\":\"Barbados\",\"dial_code\":\"+1 246\",\"code\":\"BB\"},{\"name\":\"Belarus\",\"dial_code\":\"+375\",\"code\":\"BY\"},{\"name\":\"Belgium\",\"dial_code\":\"+32\",\"code\":\"BE\"},{\"name\":\"Belize\",\"dial_code\":\"+501\",\"code\":\"BZ\"},{\"name\":\"Benin\",\"dial_code\":\"+229\",\"code\":\"BJ\"},{\"name\":\"Bermuda\",\"dial_code\":\"+1 441\",\"code\":\"BM\"},{\"name\":\"Bhutan\",\"dial_code\":\"+975\",\"code\":\"BT\"},{\"name\":\"Bosnia and Herzegovina\",\"dial_code\":\"+387\",\"code\":\"BA\"},{\"name\":\"Botswana\",\"dial_code\":\"+267\",\"code\":\"BW\"},{\"name\":\"Brazil\",\"dial_code\":\"+55\",\"code\":\"BR\"},{\"name\":\"British Indian Ocean Territory\",\"dial_code\":\"+246\",\"code\":\"IO\"},{\"name\":\"Bulgaria\",\"dial_code\":\"+359\",\"code\":\"BG\"},{\"name\":\"Burkina Faso\",\"dial_code\":\"+226\",\"code\":\"BF\"},{\"name\":\"Burundi\",\"dial_code\":\"+257\",\"code\":\"BI\"},{\"name\":\"Cambodia\",\"dial_code\":\"+855\",\"code\":\"KH\"},{\"name\":\"Cameroon\",\"dial_code\":\"+237\",\"code\":\"CM\"},{\"name\":\"Canada\",\"dial_code\":\"+1\",\"code\":\"CA\"},{\"name\":\"Cape Verde\",\"dial_code\":\"+238\",\"code\":\"CV\"},{\"name\":\"Cayman Islands\",\"dial_code\":\"+ 345\",\"code\":\"KY\"},{\"name\":\"Central African Republic\",\"dial_code\":\"+236\",\"code\":\"CF\"},{\"name\":\"Chad\",\"dial_code\":\"+235\",\"code\":\"TD\"},{\"name\":\"Chile\",\"dial_code\":\"+56\",\"code\":\"CL\"},{\"name\":\"China\",\"dial_code\":\"+86\",\"code\":\"CN\"},{\"name\":\"Christmas Island\",\"dial_code\":\"+61\",\"code\":\"CX\"},{\"name\":\"Colombia\",\"dial_code\":\"+57\",\"code\":\"CO\"},{\"name\":\"Comoros\",\"dial_code\":\"+269\",\"code\":\"KM\"},{\"name\":\"Congo\",\"dial_code\":\"+242\",\"code\":\"CG\"},{\"name\":\"Cook Islands\",\"dial_code\":\"+682\",\"code\":\"CK\"},{\"name\":\"Costa Rica\",\"dial_code\":\"+506\",\"code\":\"CR\"},{\"name\":\"Croatia\",\"dial_code\":\"+385\",\"code\":\"HR\"},{\"name\":\"Cuba\",\"dial_code\":\"+53\",\"code\":\"CU\"},{\"name\":\"Cyprus\",\"dial_code\":\"+537\",\"code\":\"CY\"},{\"name\":\"Czech Republic\",\"dial_code\":\"+420\",\"code\":\"CZ\"},{\"name\":\"Denmark\",\"dial_code\":\"+45\",\"code\":\"DK\"},{\"name\":\"Djibouti\",\"dial_code\":\"+253\",\"code\":\"DJ\"},{\"name\":\"Dominica\",\"dial_code\":\"+1 767\",\"code\":\"DM\"},{\"name\":\"Dominican Republic\",\"dial_code\":\"+1 849\",\"code\":\"DO\"},{\"name\":\"Ecuador\",\"dial_code\":\"+593\",\"code\":\"EC\"},{\"name\":\"Egypt\",\"dial_code\":\"+20\",\"code\":\"EG\"},{\"name\":\"El Salvador\",\"dial_code\":\"+503\",\"code\":\"SV\"},{\"name\":\"Equatorial Guinea\",\"dial_code\":\"+240\",\"code\":\"GQ\"},{\"name\":\"Eritrea\",\"dial_code\":\"+291\",\"code\":\"ER\"},{\"name\":\"Estonia\",\"dial_code\":\"+372\",\"code\":\"EE\"},{\"name\":\"Ethiopia\",\"dial_code\":\"+251\",\"code\":\"ET\"},{\"name\":\"Faroe Islands\",\"dial_code\":\"+298\",\"code\":\"FO\"},{\"name\":\"Fiji\",\"dial_code\":\"+679\",\"code\":\"FJ\"},{\"name\":\"Finland\",\"dial_code\":\"+358\",\"code\":\"FI\"},{\"name\":\"France\",\"dial_code\":\"+33\",\"code\":\"FR\"},{\"name\":\"French Guiana\",\"dial_code\":\"+594\",\"code\":\"GF\"},{\"name\":\"French Polynesia\",\"dial_code\":\"+689\",\"code\":\"PF\"},{\"name\":\"Gabon\",\"dial_code\":\"+241\",\"code\":\"GA\"},{\"name\":\"Gambia\",\"dial_code\":\"+220\",\"code\":\"GM\"},{\"name\":\"Georgia\",\"dial_code\":\"+995\",\"code\":\"GE\"},{\"name\":\"Germany\",\"dial_code\":\"+49\",\"code\":\"DE\"},{\"name\":\"Ghana\",\"dial_code\":\"+233\",\"code\":\"GH\"},{\"name\":\"Gibraltar\",\"dial_code\":\"+350\",\"code\":\"GI\"},{\"name\":\"Greece\",\"dial_code\":\"+30\",\"code\":\"GR\"},{\"name\":\"Greenland\",\"dial_code\":\"+299\",\"code\":\"GL\"},{\"name\":\"Grenada\",\"dial_code\":\"+1 473\",\"code\":\"GD\"},{\"name\":\"Guadeloupe\",\"dial_code\":\"+590\",\"code\":\"GP\"},{\"name\":\"Guam\",\"dial_code\":\"+1 671\",\"code\":\"GU\"},{\"name\":\"Guatemala\",\"dial_code\":\"+502\",\"code\":\"GT\"},{\"name\":\"Guinea\",\"dial_code\":\"+224\",\"code\":\"GN\"},{\"name\":\"Guinea-Bissau\",\"dial_code\":\"+245\",\"code\":\"GW\"},{\"name\":\"Guyana\",\"dial_code\":\"+595\",\"code\":\"GY\"},{\"name\":\"Haiti\",\"dial_code\":\"+509\",\"code\":\"HT\"},{\"name\":\"Honduras\",\"dial_code\":\"+504\",\"code\":\"HN\"},{\"name\":\"Hungary\",\"dial_code\":\"+36\",\"code\":\"HU\"},{\"name\":\"Iceland\",\"dial_code\":\"+354\",\"code\":\"IS\"},{\"name\":\"India\",\"dial_code\":\"+91\",\"code\":\"IN\"},{\"name\":\"Indonesia\",\"dial_code\":\"+62\",\"code\":\"ID\"},{\"name\":\"Iraq\",\"dial_code\":\"+964\",\"code\":\"IQ\"},{\"name\":\"Ireland\",\"dial_code\":\"+353\",\"code\":\"IE\"},{\"name\":\"Israel\",\"dial_code\":\"+972\",\"code\":\"IL\"},{\"name\":\"Italy\",\"dial_code\":\"+39\",\"code\":\"IT\"},{\"name\":\"Jamaica\",\"dial_code\":\"+1 876\",\"code\":\"JM\"},{\"name\":\"Japan\",\"dial_code\":\"+81\",\"code\":\"JP\"},{\"name\":\"Jordan\",\"dial_code\":\"+962\",\"code\":\"JO\"},{\"name\":\"Kazakhstan\",\"dial_code\":\"+7 7\",\"code\":\"KZ\"},{\"name\":\"Kenya\",\"dial_code\":\"+254\",\"code\":\"KE\"},{\"name\":\"Kiribati\",\"dial_code\":\"+686\",\"code\":\"KI\"},{\"name\":\"Kuwait\",\"dial_code\":\"+965\",\"code\":\"KW\"},{\"name\":\"Kyrgyzstan\",\"dial_code\":\"+996\",\"code\":\"KG\"},{\"name\":\"Latvia\",\"dial_code\":\"+371\",\"code\":\"LV\"},{\"name\":\"Lebanon\",\"dial_code\":\"+961\",\"code\":\"LB\"},{\"name\":\"Lesotho\",\"dial_code\":\"+266\",\"code\":\"LS\"},{\"name\":\"Liberia\",\"dial_code\":\"+231\",\"code\":\"LR\"},{\"name\":\"Liechtenstein\",\"dial_code\":\"+423\",\"code\":\"LI\"},{\"name\":\"Lithuania\",\"dial_code\":\"+370\",\"code\":\"LT\"},{\"name\":\"Luxembourg\",\"dial_code\":\"+352\",\"code\":\"LU\"},{\"name\":\"Madagascar\",\"dial_code\":\"+261\",\"code\":\"MG\"},{\"name\":\"Malawi\",\"dial_code\":\"+265\",\"code\":\"MW\"},{\"name\":\"Malaysia\",\"dial_code\":\"+60\",\"code\":\"MY\"},{\"name\":\"Maldives\",\"dial_code\":\"+960\",\"code\":\"MV\"},{\"name\":\"Mali\",\"dial_code\":\"+223\",\"code\":\"ML\"},{\"name\":\"Malta\",\"dial_code\":\"+356\",\"code\":\"MT\"},{\"name\":\"Marshall Islands\",\"dial_code\":\"+692\",\"code\":\"MH\"},{\"name\":\"Martinique\",\"dial_code\":\"+596\",\"code\":\"MQ\"},{\"name\":\"Mauritania\",\"dial_code\":\"+222\",\"code\":\"MR\"},{\"name\":\"Mauritius\",\"dial_code\":\"+230\",\"code\":\"MU\"},{\"name\":\"Mayotte\",\"dial_code\":\"+262\",\"code\":\"YT\"},{\"name\":\"Mexico\",\"dial_code\":\"+52\",\"code\":\"MX\"},{\"name\":\"Monaco\",\"dial_code\":\"+377\",\"code\":\"MC\"},{\"name\":\"Mongolia\",\"dial_code\":\"+976\",\"code\":\"MN\"},{\"name\":\"Montenegro\",\"dial_code\":\"+382\",\"code\":\"ME\"},{\"name\":\"Montserrat\",\"dial_code\":\"+1664\",\"code\":\"MS\"},{\"name\":\"Morocco\",\"dial_code\":\"+212\",\"code\":\"MA\"},{\"name\":\"Myanmar\",\"dial_code\":\"+95\",\"code\":\"MM\"},{\"name\":\"Namibia\",\"dial_code\":\"+264\",\"code\":\"NA\"},{\"name\":\"Nauru\",\"dial_code\":\"+674\",\"code\":\"NR\"},{\"name\":\"Nepal\",\"dial_code\":\"+977\",\"code\":\"NP\"},{\"name\":\"Netherlands\",\"dial_code\":\"+31\",\"code\":\"NL\"},{\"name\":\"Netherlands Antilles\",\"dial_code\":\"+599\",\"code\":\"AN\"},{\"name\":\"New Caledonia\",\"dial_code\":\"+687\",\"code\":\"NC\"},{\"name\":\"New Zealand\",\"dial_code\":\"+64\",\"code\":\"NZ\"},{\"name\":\"Nicaragua\",\"dial_code\":\"+505\",\"code\":\"NI\"},{\"name\":\"Niger\",\"dial_code\":\"+227\",\"code\":\"NE\"},{\"name\":\"Nigeria\",\"dial_code\":\"+234\",\"code\":\"NG\"},{\"name\":\"Niue\",\"dial_code\":\"+683\",\"code\":\"NU\"},{\"name\":\"Norfolk Island\",\"dial_code\":\"+672\",\"code\":\"NF\"},{\"name\":\"Northern Mariana Islands\",\"dial_code\":\"+1 670\",\"code\":\"MP\"},{\"name\":\"Norway\",\"dial_code\":\"+47\",\"code\":\"NO\"},{\"name\":\"Oman\",\"dial_code\":\"+968\",\"code\":\"OM\"},{\"name\":\"Pakistan\",\"dial_code\":\"+92\",\"code\":\"PK\"},{\"name\":\"Palau\",\"dial_code\":\"+680\",\"code\":\"PW\"},{\"name\":\"Panama\",\"dial_code\":\"+507\",\"code\":\"PA\"},{\"name\":\"Papua New Guinea\",\"dial_code\":\"+675\",\"code\":\"PG\"},{\"name\":\"Paraguay\",\"dial_code\":\"+595\",\"code\":\"PY\"},{\"name\":\"Peru\",\"dial_code\":\"+51\",\"code\":\"PE\"},{\"name\":\"Philippines\",\"dial_code\":\"+63\",\"code\":\"PH\"},{\"name\":\"Poland\",\"dial_code\":\"+48\",\"code\":\"PL\"},{\"name\":\"Portugal\",\"dial_code\":\"+351\",\"code\":\"PT\"},{\"name\":\"Puerto Rico\",\"dial_code\":\"+1 939\",\"code\":\"PR\"},{\"name\":\"Qatar\",\"dial_code\":\"+974\",\"code\":\"QA\"},{\"name\":\"Romania\",\"dial_code\":\"+40\",\"code\":\"RO\"},{\"name\":\"Rwanda\",\"dial_code\":\"+250\",\"code\":\"RW\"},{\"name\":\"Samoa\",\"dial_code\":\"+685\",\"code\":\"WS\"},{\"name\":\"San Marino\",\"dial_code\":\"+378\",\"code\":\"SM\"},{\"name\":\"Saudi Arabia\",\"dial_code\":\"+966\",\"code\":\"SA\"},{\"name\":\"Senegal\",\"dial_code\":\"+221\",\"code\":\"SN\"},{\"name\":\"Serbia\",\"dial_code\":\"+381\",\"code\":\"RS\"},{\"name\":\"Seychelles\",\"dial_code\":\"+248\",\"code\":\"SC\"},{\"name\":\"Sierra Leone\",\"dial_code\":\"+232\",\"code\":\"SL\"},{\"name\":\"Singapore\",\"dial_code\":\"+65\",\"code\":\"SG\"},{\"name\":\"Slovakia\",\"dial_code\":\"+421\",\"code\":\"SK\"},{\"name\":\"Slovenia\",\"dial_code\":\"+386\",\"code\":\"SI\"},{\"name\":\"Solomon Islands\",\"dial_code\":\"+677\",\"code\":\"SB\"},{\"name\":\"South Africa\",\"dial_code\":\"+27\",\"code\":\"ZA\"},{\"name\":\"South Georgia and the South Sandwich Islands\",\"dial_code\":\"+500\",\"code\":\"GS\"},{\"name\":\"Spain\",\"dial_code\":\"+34\",\"code\":\"ES\"},{\"name\":\"Sri Lanka\",\"dial_code\":\"+94\",\"code\":\"LK\"},{\"name\":\"Sudan\",\"dial_code\":\"+249\",\"code\":\"SD\"},{\"name\":\"Suriname\",\"dial_code\":\"+597\",\"code\":\"SR\"},{\"name\":\"Swaziland\",\"dial_code\":\"+268\",\"code\":\"SZ\"},{\"name\":\"Sweden\",\"dial_code\":\"+46\",\"code\":\"SE\"},{\"name\":\"Switzerland\",\"dial_code\":\"+41\",\"code\":\"CH\"},{\"name\":\"Tajikistan\",\"dial_code\":\"+992\",\"code\":\"TJ\"},{\"name\":\"Thailand\",\"dial_code\":\"+66\",\"code\":\"TH\"},{\"name\":\"Togo\",\"dial_code\":\"+228\",\"code\":\"TG\"},{\"name\":\"Tokelau\",\"dial_code\":\"+690\",\"code\":\"TK\"},{\"name\":\"Tonga\",\"dial_code\":\"+676\",\"code\":\"TO\"},{\"name\":\"Trinidad and Tobago\",\"dial_code\":\"+1 868\",\"code\":\"TT\"},{\"name\":\"Tunisia\",\"dial_code\":\"+216\",\"code\":\"TN\"},{\"name\":\"Turkey\",\"dial_code\":\"+90\",\"code\":\"TR\"},{\"name\":\"Turkmenistan\",\"dial_code\":\"+993\",\"code\":\"TM\"},{\"name\":\"Turks and Caicos Islands\",\"dial_code\":\"+1 649\",\"code\":\"TC\"},{\"name\":\"Tuvalu\",\"dial_code\":\"+688\",\"code\":\"TV\"},{\"name\":\"Uganda\",\"dial_code\":\"+256\",\"code\":\"UG\"},{\"name\":\"Ukraine\",\"dial_code\":\"+380\",\"code\":\"UA\"},{\"name\":\"United Arab Emirates\",\"dial_code\":\"+971\",\"code\":\"AE\"},{\"name\":\"United Kingdom\",\"dial_code\":\"+44\",\"code\":\"GB\"},{\"name\":\"United States\",\"dial_code\":\"+1\",\"code\":\"US\"},{\"name\":\"Uruguay\",\"dial_code\":\"+598\",\"code\":\"UY\"},{\"name\":\"Uzbekistan\",\"dial_code\":\"+998\",\"code\":\"UZ\"},{\"name\":\"Vanuatu\",\"dial_code\":\"+678\",\"code\":\"VU\"},{\"name\":\"Wallis and Futuna\",\"dial_code\":\"+681\",\"code\":\"WF\"},{\"name\":\"Yemen\",\"dial_code\":\"+967\",\"code\":\"YE\"},{\"name\":\"Zambia\",\"dial_code\":\"+260\",\"code\":\"ZM\"},{\"name\":\"Zimbabwe\",\"dial_code\":\"+263\",\"code\":\"ZW\"},{\"name\":\"land Islands\",\"dial_code\":\"\",\"code\":\"AX\"},{\"name\":\"Antarctica\",\"dial_code\":null,\"code\":\"AQ\"},{\"name\":\"Bolivia, Plurinational State of\",\"dial_code\":\"+591\",\"code\":\"BO\"},{\"name\":\"Brunei Darussalam\",\"dial_code\":\"+673\",\"code\":\"BN\"},{\"name\":\"Cocos (Keeling) Islands\",\"dial_code\":\"+61\",\"code\":\"CC\"},{\"name\":\"Congo, The Democratic Republic of the\",\"dial_code\":\"+243\",\"code\":\"CD\"},{\"name\":\"Cote d'Ivoire\",\"dial_code\":\"+225\",\"code\":\"CI\"},{\"name\":\"Falkland Islands (Malvinas)\",\"dial_code\":\"+500\",\"code\":\"FK\"},{\"name\":\"Guernsey\",\"dial_code\":\"+44\",\"code\":\"GG\"},{\"name\":\"Holy See (Vatican City State)\",\"dial_code\":\"+379\",\"code\":\"VA\"},{\"name\":\"Hong Kong\",\"dial_code\":\"+852\",\"code\":\"HK\"},{\"name\":\"Iran, Islamic Republic of\",\"dial_code\":\"+98\",\"code\":\"IR\"},{\"name\":\"Isle of Man\",\"dial_code\":\"+44\",\"code\":\"IM\"},{\"name\":\"Jersey\",\"dial_code\":\"+44\",\"code\":\"JE\"},{\"name\":\"Korea, Democratic People's Republic of\",\"dial_code\":\"+850\",\"code\":\"KP\"},{\"name\":\"Korea, Republic of\",\"dial_code\":\"+82\",\"code\":\"KR\"},{\"name\":\"Lao People's Democratic Republic\",\"dial_code\":\"+856\",\"code\":\"LA\"},{\"name\":\"Libyan Arab Jamahiriya\",\"dial_code\":\"+218\",\"code\":\"LY\"},{\"name\":\"Macao\",\"dial_code\":\"+853\",\"code\":\"MO\"},{\"name\":\"Macedonia, The Former Yugoslav Republic of\",\"dial_code\":\"+389\",\"code\":\"MK\"},{\"name\":\"Micronesia, Federated States of\",\"dial_code\":\"+691\",\"code\":\"FM\"},{\"name\":\"Moldova, Republic of\",\"dial_code\":\"+373\",\"code\":\"MD\"},{\"name\":\"Mozambique\",\"dial_code\":\"+258\",\"code\":\"MZ\"},{\"name\":\"Palestinian Territory, Occupied\",\"dial_code\":\"+970\",\"code\":\"PS\"},{\"name\":\"Pitcairn\",\"dial_code\":\"+872\",\"code\":\"PN\"},{\"name\":\"Réunion\",\"dial_code\":\"+262\",\"code\":\"RE\"},{\"name\":\"Russia\",\"dial_code\":\"+7\",\"code\":\"RU\"},{\"name\":\"Saint Barthélemy\",\"dial_code\":\"+590\",\"code\":\"BL\"},{\"name\":\"Saint Helena, Ascension and Tristan Da Cunha\",\"dial_code\":\"+290\",\"code\":\"SH\"},{\"name\":\"Saint Kitts and Nevis\",\"dial_code\":\"+1 869\",\"code\":\"KN\"},{\"name\":\"Saint Lucia\",\"dial_code\":\"+1 758\",\"code\":\"LC\"},{\"name\":\"Saint Martin\",\"dial_code\":\"+590\",\"code\":\"MF\"},{\"name\":\"Saint Pierre and Miquelon\",\"dial_code\":\"+508\",\"code\":\"PM\"},{\"name\":\"Saint Vincent and the Grenadines\",\"dial_code\":\"+1 784\",\"code\":\"VC\"},{\"name\":\"Sao Tome and Principe\",\"dial_code\":\"+239\",\"code\":\"ST\"},{\"name\":\"Somalia\",\"dial_code\":\"+252\",\"code\":\"SO\"},{\"name\":\"Svalbard and Jan Mayen\",\"dial_code\":\"+47\",\"code\":\"SJ\"},{\"name\":\"Syrian Arab Republic\",\"dial_code\":\"+963\",\"code\":\"SY\"},{\"name\":\"Taiwan, Province of China\",\"dial_code\":\"+886\",\"code\":\"TW\"},{\"name\":\"Tanzania, United Republic of\",\"dial_code\":\"+255\",\"code\":\"TZ\"},{\"name\":\"Timor-Leste\",\"dial_code\":\"+670\",\"code\":\"TL\"},{\"name\":\"Venezuela, Bolivarian Republic of\",\"dial_code\":\"+58\",\"code\":\"VE\"},{\"name\":\"Viet Nam\",\"dial_code\":\"+84\",\"code\":\"VN\"},{\"name\":\"Virgin Islands, British\",\"dial_code\":\"+1 284\",\"code\":\"VG\"},{\"name\":\"Virgin Islands, U.S.\",\"dial_code\":\"+1 340\",\"code\":\"VI\"}]}"


    @JvmStatic
    fun isEmpty(content: String?): Boolean {
        return content == null || "".equals(content)
    }

    @JvmStatic
    fun isValidEmailAddress(email: String): Boolean {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)

        return matcher.matches()
    }


    var reformatted = ""
    fun fomatDateNormal(date: String): String {
        try {
            var fromUser = SimpleDateFormat(TIME_FORMAT)
            val myFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy")
//            var fromUser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//            val myFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
            fromUser.setTimeZone(TimeZone.getDefault())
            try {
                reformatted = myFormat.format(fromUser.parse(date))
                return reformatted

            } catch (e: ParseException) {
                e.printStackTrace()
                fromUser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                fromUser.setTimeZone(TimeZone.getDefault())
                reformatted = myFormat.format(fromUser.parse(date))
                return reformatted
            }
        } catch (e: ParseException) {

        }
        return reformatted

    }

    fun formatTime(originalString: String): Date {
        try {
            return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(originalString)
        } catch (e: ParseException) {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString)
        }
    }

    var reformattedSub = ""
    fun fomatDateSb(date: String): String {
        var fromUser = SimpleDateFormat(TIME_FORMAT)
        val myFormat = SimpleDateFormat("EEE, MMM dd, yyyy")
//        val myFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
//        fromUser.setTimeZone(TimeZone.getTimeZone("UTC"))
//        myFormat.setTimeZone(TimeZone.getDefault())
        try {
            reformattedSub = myFormat.format(fromUser.parse(date))
            return reformattedSub

        } catch (e: ParseException) {
            e.printStackTrace()
            fromUser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            fromUser.setTimeZone(TimeZone.getTimeZone("UTC"))
//            myFormat.setTimeZone(TimeZone.getDefault())
            reformattedSub = myFormat.format(fromUser.parse(date))
            return reformattedSub
        }
        return reformattedSub
    }

    var reformattedTime = ""
    fun fomatTimeNormal(date: String): String {
        val fromUser = SimpleDateFormat(TIME_FORMAT)
        val myFormat = SimpleDateFormat("HH:mm")
//        val myFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
//        fromUser.setTimeZone(TimeZone.getTimeZone("UTC"))
//        myFormat.setTimeZone(TimeZone.getDefault())
        try {
            reformatted = myFormat.format(fromUser.parse(date))
            return reformatted

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return reformattedTime
    }

    fun getLibraryMasterCountriesEnglish(): MutableList<CCPCountry> {
        val countries = java.util.ArrayList<CCPCountry>()
        countries.add(CCPCountry("ad", "376", "Andorra", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ae", "971", "United Arab Emirates (UAE)", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("af", "93", "Afghanistan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ag", "1", "Antigua and Barbuda", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ai", "1", "Anguilla", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("al", "355", "Albania", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("am", "374", "Armenia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ao", "244", "Angola", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("aq", "672", "Antarctica", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ar", "54", "Argentina", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("as", "1", "American Samoa", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("at", "43", "Austria", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("au", "61", "Australia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("aw", "297", "Aruba", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("az", "358", "Aland Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("az", "994", "Azerbaijan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ba", "387", "Bosnia And Herzegovina", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bb", "1", "Barbados", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bd", "880", "Bangladesh", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("be", "32", "Belgium", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bf", "226", "Burkina Faso", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bg", "359", "Bulgaria", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bh", "973", "Bahrain", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bi", "257", "Burundi", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bj", "229", "Benin", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bl", "590", "Saint Barthélemy", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bm", "1", "Bermuda", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bn", "673", "Brunei Darussalam", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bo", "591", "Bolivia, Plurinational State Of", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("br", "55", "Brazil", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bs", "1", "Bahamas", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bt", "975", "Bhutan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bw", "267", "Botswana", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("by", "375", "Belarus", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("bz", "501", "Belize", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ca", "1", "Canada", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cc", "61", "Cocos (keeling) Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cd", "243", "Congo, The Democratic Republic Of The", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cf", "236", "Central African Republic", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cg", "242", "Congo", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ch", "41", "Switzerland", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ci", "225", "Côte D'ivoire", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ck", "682", "Cook Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cl", "56", "Chile", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cm", "237", "Cameroon", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cn", "86", "China", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("co", "57", "Colombia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cr", "506", "Costa Rica", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cu", "53", "Cuba", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cv", "238", "Cape Verde", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cx", "61", "Christmas Island", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cy", "357", "Cyprus", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("cz", "420", "Czech Republic", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("de", "49", "Germany", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("dj", "253", "Djibouti", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("dk", "45", "Denmark", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("dm", "1", "Dominica", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("do", "1", "Dominican Republic", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("dz", "213", "Algeria", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ec", "593", "Ecuador", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ee", "372", "Estonia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("eg", "20", "Egypt", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("er", "291", "Eritrea", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("es", "34", "Spain", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("et", "251", "Ethiopia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("fi", "358", "Finland", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("fj", "679", "Fiji", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("fk", "500", "Falkland Islands (malvinas)", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("fm", "691", "Micronesia, Federated States Of", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("fo", "298", "Faroe Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("fr", "33", "France", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ga", "241", "Gabon", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gb", "44", "United Kingdom", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gd", "1", "Grenada", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ge", "995", "Georgia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gf", "594", "French Guyana", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gh", "233", "Ghana", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gi", "350", "Gibraltar", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gl", "299", "Greenland", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gm", "220", "Gambia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gn", "224", "Guinea", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gp", "450", "Guadeloupe", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gq", "240", "Equatorial Guinea", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gr", "30", "Greece", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gt", "502", "Guatemala", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gu", "1", "Guam", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gw", "245", "Guinea-bissau", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("gy", "592", "Guyana", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("hk", "852", "Hong Kong", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("hn", "504", "Honduras", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("hr", "385", "Croatia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ht", "509", "Haiti", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("hu", "36", "Hungary", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("id", "62", "Indonesia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ie", "353", "Ireland", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("il", "972", "Israel", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("im", "44", "Isle Of Man", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("is", "354", "Iceland", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("in", "91", "India", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("io", "246", "British Indian Ocean Territory", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("iq", "964", "Iraq", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ir", "98", "Iran, Islamic Republic Of", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("it", "39", "Italy", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("je", "44", "Jersey ", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("jm", "1", "Jamaica", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("jo", "962", "Jordan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("jp", "81", "Japan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ke", "254", "Kenya", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("kg", "996", "Kyrgyzstan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("kh", "855", "Cambodia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ki", "686", "Kiribati", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("km", "269", "Comoros", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("kn", "1", "Saint Kitts and Nevis", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("kp", "850", "North Korea", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("kr", "82", "South Korea", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("kw", "965", "Kuwait", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ky", "1", "Cayman Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("kz", "7", "Kazakhstan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("la", "856", "Lao People's Democratic Republic", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("lb", "961", "Lebanon", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("lc", "1", "Saint Lucia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("li", "423", "Liechtenstein", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("lk", "94", "Sri Lanka", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("lr", "231", "Liberia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ls", "266", "Lesotho", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("lt", "370", "Lithuania", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("lu", "352", "Luxembourg", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("lv", "371", "Latvia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ly", "218", "Libya", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ma", "212", "Morocco", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mc", "377", "Monaco", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("md", "373", "Moldova, Republic Of", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("me", "382", "Montenegro", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mf", "590", "Saint Martin", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mg", "261", "Madagascar", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mh", "692", "Marshall Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mk", "389", "Macedonia, The Former Yugoslav Republic Of", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ml", "223", "Mali", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mm", "95", "Myanmar", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mn", "976", "Mongolia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mo", "853", "Macao", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mp", "1", "Northern Mariana Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mq", "596", "Martinique", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mr", "222", "Mauritania", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ms", "1", "Montserrat", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mt", "356", "Malta", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mu", "230", "Mauritius", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mv", "960", "Maldives", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mw", "265", "Malawi", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mx", "52", "Mexico", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("my", "60", "Malaysia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("mz", "258", "Mozambique", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("na", "264", "Namibia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("nc", "687", "New Caledonia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ne", "227", "Niger", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("nf", "672", "Norfolk Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ng", "234", "Nigeria", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ni", "505", "Nicaragua", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("nl", "31", "Netherlands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("no", "47", "Norway", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("np", "977", "Nepal", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("nr", "674", "Nauru", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("nu", "683", "Niue", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("nz", "64", "New Zealand", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("om", "968", "Oman", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pa", "507", "Panama", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pe", "51", "Peru", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pf", "689", "French Polynesia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pg", "675", "Papua New Guinea", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ph", "63", "Philippines", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pk", "92", "Pakistan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pl", "48", "Poland", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pm", "508", "Saint Pierre And Miquelon", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pn", "870", "Pitcairn", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pr", "1", "Puerto Rico", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ps", "970", "Palestine", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pt", "351", "Portugal", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("pw", "680", "Palau", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("py", "595", "Paraguay", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("qa", "974", "Qatar", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("re", "262", "Réunion", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ro", "40", "Romania", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("rs", "381", "Serbia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ru", "7", "Russian Federation", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("rw", "250", "Rwanda", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sa", "966", "Saudi Arabia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sb", "677", "Solomon Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sc", "248", "Seychelles", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sd", "249", "Sudan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("se", "46", "Sweden", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sg", "65", "Singapore", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sh", "290", "Saint Helena, Ascension And Tristan Da Cunha", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("si", "386", "Slovenia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sk", "421", "Slovakia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sl", "232", "Sierra Leone", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sm", "378", "San Marino", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sn", "221", "Senegal", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("so", "252", "Somalia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sr", "597", "Suriname", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ss", "211", "South Sudan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("st", "239", "Sao Tome And Principe", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sv", "503", "El Salvador", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sx", "1", "Sint Maarten", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sy", "963", "Syrian Arab Republic", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("sz", "268", "Swaziland", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tc", "1", "Turks and Caicos Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("td", "235", "Chad", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tg", "228", "Togo", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("th", "66", "Thailand", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tj", "992", "Tajikistan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tk", "690", "Tokelau", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tl", "670", "Timor-leste", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tm", "993", "Turkmenistan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tn", "216", "Tunisia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("to", "676", "Tonga", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tr", "90", "Turkey", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tt", "1", "Trinidad &amp; Tobago", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tv", "688", "Tuvalu", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tw", "886", "Taiwan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("tz", "255", "Tanzania, United Republic Of", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ua", "380", "Ukraine", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ug", "256", "Uganda", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("us", "1", "United States", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("uy", "598", "Uruguay", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("uz", "998", "Uzbekistan", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("va", "379", "Holy See (vatican City State)", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("vc", "1", "Saint Vincent &amp; The Grenadines", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ve", "58", "Venezuela, Bolivarian Republic Of", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("vg", "1", "British Virgin Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("vi", "1", "US Virgin Islands", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("vn", "84", "Viet Nam", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("vu", "678", "Vanuatu", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("wf", "681", "Wallis And Futuna", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ws", "685", "Samoa", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("xk", "383", "Kosovo", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("ye", "967", "Yemen", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("yt", "262", "Mayotte", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("za", "27", "South Africa", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("zm", "260", "Zambia", DEFAULT_FLAG_RES))
        countries.add(CCPCountry("zw", "263", "Zimbabwe", DEFAULT_FLAG_RES))
        return countries
    }


    fun getFlagMasterResID(id: String): Int {
        when (id) {
            //this should be sorted based on country name code.
            "ad" //andorra
            -> return R.drawable.flag_andorra
            "ae" //united arab emirates
            -> return R.drawable.flag_uae
            "af" //afghanistan
            -> return R.drawable.flag_afghanistan
            "ag" //antigua & barbuda
            -> return R.drawable.flag_antigua_and_barbuda
            "ai" //anguilla // Caribbean Islands
            -> return R.drawable.flag_anguilla
            "al" //albania
            -> return R.drawable.flag_albania
            "am" //armenia
            -> return R.drawable.flag_armenia
            "ao" //angola
            -> return R.drawable.flag_angola
            "aq" //antarctica // custom
            -> return R.drawable.flag_antarctica
            "ar" //argentina
            -> return R.drawable.flag_argentina
            "as" //American Samoa
            -> return R.drawable.flag_american_samoa
            "at" //austria
            -> return R.drawable.flag_austria
            "au" //australia
            -> return R.drawable.flag_australia
            "aw" //aruba
            -> return R.drawable.flag_aruba
            "ax" //alan islands
            -> return R.drawable.flag_aland
            "az" //azerbaijan
            -> return R.drawable.flag_azerbaijan
            "ba" //bosnia and herzegovina
            -> return R.drawable.flag_bosnia
            "bb" //barbados
            -> return R.drawable.flag_barbados
            "bd" //bangladesh
            -> return R.drawable.flag_bangladesh
            "be" //belgium
            -> return R.drawable.flag_belgium
            "bf" //burkina faso
            -> return R.drawable.flag_burkina_faso
            "bg" //bulgaria
            -> return R.drawable.flag_bulgaria
            "bh" //bahrain
            -> return R.drawable.flag_bahrain
            "bi" //burundi
            -> return R.drawable.flag_burundi
            "bj" //benin
            -> return R.drawable.flag_benin
            "bl" //saint barthélemy
            -> return R.drawable.flag_saint_barthelemy// custom
            "bm" //bermuda
            -> return R.drawable.flag_bermuda
            "bn" //brunei darussalam // custom
            -> return R.drawable.flag_brunei
            "bo" //bolivia, plurinational state of
            -> return R.drawable.flag_bolivia
            "br" //brazil
            -> return R.drawable.flag_brazil
            "bs" //bahamas
            -> return R.drawable.flag_bahamas
            "bt" //bhutan
            -> return R.drawable.flag_bhutan
            "bw" //botswana
            -> return R.drawable.flag_botswana
            "by" //belarus
            -> return R.drawable.flag_belarus
            "bz" //belize
            -> return R.drawable.flag_belize
            "ca" //canada
            -> return R.drawable.flag_canada
            "cc" //cocos (keeling) islands
            -> return R.drawable.flag_cocos// custom
            "cd" //congo, the democratic republic of the
            -> return R.drawable.flag_democratic_republic_of_the_congo
            "cf" //central african republic
            -> return R.drawable.flag_central_african_republic
            "cg" //congo
            -> return R.drawable.flag_republic_of_the_congo
            "ch" //switzerland
            -> return R.drawable.flag_switzerland
            "ci" //côte d\'ivoire
            -> return R.drawable.flag_cote_divoire
            "ck" //cook islands
            -> return R.drawable.flag_cook_islands
            "cl" //chile
            -> return R.drawable.flag_chile
            "cm" //cameroon
            -> return R.drawable.flag_cameroon
            "cn" //china
            -> return R.drawable.flag_china
            "co" //colombia
            -> return R.drawable.flag_colombia
            "cr" //costa rica
            -> return R.drawable.flag_costa_rica
            "cu" //cuba
            -> return R.drawable.flag_cuba
            "cv" //cape verde
            -> return R.drawable.flag_cape_verde
            "cx" //christmas island
            -> return R.drawable.flag_christmas_island
            "cy" //cyprus
            -> return R.drawable.flag_cyprus
            "cz" //czech republic
            -> return R.drawable.flag_czech_republic
            "de" //germany
            -> return R.drawable.flag_germany
            "dj" //djibouti
            -> return R.drawable.flag_djibouti
            "dk" //denmark
            -> return R.drawable.flag_denmark
            "dm" //dominica
            -> return R.drawable.flag_dominica
            "do" //dominican republic
            -> return R.drawable.flag_dominican_republic
            "dz" //algeria
            -> return R.drawable.flag_algeria
            "ec" //ecuador
            -> return R.drawable.flag_ecuador
            "ee" //estonia
            -> return R.drawable.flag_estonia
            "eg" //egypt
            -> return R.drawable.flag_egypt
            "er" //eritrea
            -> return R.drawable.flag_eritrea
            "es" //spain
            -> return R.drawable.flag_spain
            "et" //ethiopia
            -> return R.drawable.flag_ethiopia
            "fi" //finland
            -> return R.drawable.flag_finland
            "fj" //fiji
            -> return R.drawable.flag_fiji
            "fk" //falkland islands (malvinas)
            -> return R.drawable.flag_falkland_islands
            "fm" //micronesia, federated states of
            -> return R.drawable.flag_micronesia
            "fo" //faroe islands
            -> return R.drawable.flag_faroe_islands
            "fr" //france
            -> return R.drawable.flag_france
            "ga" //gabon
            -> return R.drawable.flag_gabon
            "gb" //united kingdom
            -> return R.drawable.flag_united_kingdom
            "gd" //grenada
            -> return R.drawable.flag_grenada
            "ge" //georgia
            -> return R.drawable.flag_georgia
            "gf" //guyane
            -> return R.drawable.flag_guyane
            "gg" //Guernsey
            -> return R.drawable.flag_guernsey
            "gh" //ghana
            -> return R.drawable.flag_ghana
            "gi" //gibraltar
            -> return R.drawable.flag_gibraltar
            "gl" //greenland
            -> return R.drawable.flag_greenland
            "gm" //gambia
            -> return R.drawable.flag_gambia
            "gn" //guinea
            -> return R.drawable.flag_guinea
            "gp" //guadeloupe
            -> return R.drawable.flag_guadeloupe
            "gq" //equatorial guinea
            -> return R.drawable.flag_equatorial_guinea
            "gr" //greece
            -> return R.drawable.flag_greece
            "gt" //guatemala
            -> return R.drawable.flag_guatemala
            "gu" //Guam
            -> return R.drawable.flag_guam
            "gw" //guinea-bissau
            -> return R.drawable.flag_guinea_bissau
            "gy" //guyana
            -> return R.drawable.flag_guyana
            "hk" //hong kong
            -> return R.drawable.flag_hong_kong
            "hn" //honduras
            -> return R.drawable.flag_honduras
            "hr" //croatia
            -> return R.drawable.flag_croatia
            "ht" //haiti
            -> return R.drawable.flag_haiti
            "hu" //hungary
            -> return R.drawable.flag_hungary
            "id" //indonesia
            -> return R.drawable.flag_indonesia
            "ie" //ireland
            -> return R.drawable.flag_ireland
            "il" //israel
            -> return R.drawable.flag_israel
            "im" //isle of man
            -> return R.drawable.flag_isleof_man // custom
            "is" //Iceland
            -> return R.drawable.flag_iceland
            "in" //india
            -> return R.drawable.flag_india
            "io" //British indian ocean territory
            -> return R.drawable.flag_british_indian_ocean_territory
            "iq" //iraq
            -> return R.drawable.flag_iraq_new
            "ir" //iran, islamic republic of
            -> return R.drawable.flag_iran
            "it" //italy
            -> return R.drawable.flag_italy
            "je" //Jersey
            -> return R.drawable.flag_jersey
            "jm" //jamaica
            -> return R.drawable.flag_jamaica
            "jo" //jordan
            -> return R.drawable.flag_jordan
            "jp" //japan
            -> return R.drawable.flag_japan
            "ke" //kenya
            -> return R.drawable.flag_kenya
            "kg" //kyrgyzstan
            -> return R.drawable.flag_kyrgyzstan
            "kh" //cambodia
            -> return R.drawable.flag_cambodia
            "ki" //kiribati
            -> return R.drawable.flag_kiribati
            "km" //comoros
            -> return R.drawable.flag_comoros
            "kn" //st kitts & nevis
            -> return R.drawable.flag_saint_kitts_and_nevis
            "kp" //north korea
            -> return R.drawable.flag_north_korea
            "kr" //south korea
            -> return R.drawable.flag_south_korea
            "kw" //kuwait
            -> return R.drawable.flag_kuwait
            "ky" //Cayman_Islands
            -> return R.drawable.flag_cayman_islands
            "kz" //kazakhstan
            -> return R.drawable.flag_kazakhstan
            "la" //lao people\'s democratic republic
            -> return R.drawable.flag_laos
            "lb" //lebanon
            -> return R.drawable.flag_lebanon
            "lc" //st lucia
            -> return R.drawable.flag_saint_lucia
            "li" //liechtenstein
            -> return R.drawable.flag_liechtenstein
            "lk" //sri lanka
            -> return R.drawable.flag_sri_lanka
            "lr" //liberia
            -> return R.drawable.flag_liberia
            "ls" //lesotho
            -> return R.drawable.flag_lesotho
            "lt" //lithuania
            -> return R.drawable.flag_lithuania
            "lu" //luxembourg
            -> return R.drawable.flag_luxembourg
            "lv" //latvia
            -> return R.drawable.flag_latvia
            "ly" //libya
            -> return R.drawable.flag_libya
            "ma" //morocco
            -> return R.drawable.flag_morocco
            "mc" //monaco
            -> return R.drawable.flag_monaco
            "md" //moldova, republic of
            -> return R.drawable.flag_moldova
            "me" //montenegro
            -> return R.drawable.flag_of_montenegro// custom
            "mf" -> return R.drawable.flag_saint_martin
            "mg" //madagascar
            -> return R.drawable.flag_madagascar
            "mh" //marshall islands
            -> return R.drawable.flag_marshall_islands
            "mk" //macedonia, the former yugoslav republic of
            -> return R.drawable.flag_macedonia
            "ml" //mali
            -> return R.drawable.flag_mali
            "mm" //myanmar
            -> return R.drawable.flag_myanmar
            "mn" //mongolia
            -> return R.drawable.flag_mongolia
            "mo" //macao
            -> return R.drawable.flag_macao
            "mp" // Northern mariana islands
            -> return R.drawable.flag_northern_mariana_islands
            "mq" //martinique
            -> return R.drawable.flag_martinique
            "mr" //mauritania
            -> return R.drawable.flag_mauritania
            "ms" //montserrat
            -> return R.drawable.flag_montserrat
            "mt" //malta
            -> return R.drawable.flag_malta
            "mu" //mauritius
            -> return R.drawable.flag_mauritius
            "mv" //maldives
            -> return R.drawable.flag_maldives
            "mw" //malawi
            -> return R.drawable.flag_malawi
            "mx" //mexico
            -> return R.drawable.flag_mexico
            "my" //malaysia
            -> return R.drawable.flag_malaysia
            "mz" //mozambique
            -> return R.drawable.flag_mozambique
            "na" //namibia
            -> return R.drawable.flag_namibia
            "nc" //new caledonia
            -> return R.drawable.flag_new_caledonia// custom
            "ne" //niger
            -> return R.drawable.flag_niger
            "nf" //Norfolk
            -> return R.drawable.flag_norfolk_island
            "ng" //nigeria
            -> return R.drawable.flag_nigeria
            "ni" //nicaragua
            -> return R.drawable.flag_nicaragua
            "nl" //netherlands
            -> return R.drawable.flag_netherlands
            "no" //norway
            -> return R.drawable.flag_norway
            "np" //nepal
            -> return R.drawable.flag_nepal
            "nr" //nauru
            -> return R.drawable.flag_nauru
            "nu" //niue
            -> return R.drawable.flag_niue
            "nz" //new zealand
            -> return R.drawable.flag_new_zealand
            "om" //oman
            -> return R.drawable.flag_oman
            "pa" //panama
            -> return R.drawable.flag_panama
            "pe" //peru
            -> return R.drawable.flag_peru
            "pf" //french polynesia
            -> return R.drawable.flag_french_polynesia
            "pg" //papua new guinea
            -> return R.drawable.flag_papua_new_guinea
            "ph" //philippines
            -> return R.drawable.flag_philippines
            "pk" //pakistan
            -> return R.drawable.flag_pakistan
            "pl" //poland
            -> return R.drawable.flag_poland
            "pm" //saint pierre and miquelon
            -> return R.drawable.flag_saint_pierre
            "pn" //pitcairn
            -> return R.drawable.flag_pitcairn_islands
            "pr" //puerto rico
            -> return R.drawable.flag_puerto_rico
            "ps" //palestine
            -> return R.drawable.flag_palestine
            "pt" //portugal
            -> return R.drawable.flag_portugal
            "pw" //palau
            -> return R.drawable.flag_palau
            "py" //paraguay
            -> return R.drawable.flag_paraguay
            "qa" //qatar
            -> return R.drawable.flag_qatar
            "re" //la reunion
            -> return R.drawable.flag_martinique // no exact flag found
            "ro" //romania
            -> return R.drawable.flag_romania
            "rs" //serbia
            -> return R.drawable.flag_serbia // custom
            "ru" //russian federation
            -> return R.drawable.flag_russian_federation
            "rw" //rwanda
            -> return R.drawable.flag_rwanda
            "sa" //saudi arabia
            -> return R.drawable.flag_saudi_arabia
            "sb" //solomon islands
            -> return R.drawable.flag_soloman_islands
            "sc" //seychelles
            -> return R.drawable.flag_seychelles
            "sd" //sudan
            -> return R.drawable.flag_sudan
            "se" //sweden
            -> return R.drawable.flag_sweden
            "sg" //singapore
            -> return R.drawable.flag_singapore
            "sh" //saint helena, ascension and tristan da cunha
            -> return R.drawable.flag_saint_helena // custom
            "si" //slovenia
            -> return R.drawable.flag_slovenia
            "sk" //slovakia
            -> return R.drawable.flag_slovakia
            "sl" //sierra leone
            -> return R.drawable.flag_sierra_leone
            "sm" //san marino
            -> return R.drawable.flag_san_marino
            "sn" //senegal
            -> return R.drawable.flag_senegal
            "so" //somalia
            -> return R.drawable.flag_somalia
            "sr" //suriname
            -> return R.drawable.flag_suriname
            "ss" //south sudan
            -> return R.drawable.flag_south_sudan
            "st" //sao tome and principe
            -> return R.drawable.flag_sao_tome_and_principe
            "sv" //el salvador
            -> return R.drawable.flag_el_salvador
            "sx" //sint maarten
            -> return R.drawable.flag_sint_maarten
            "sy" //syrian arab republic
            -> return R.drawable.flag_syria
            "sz" //swaziland
            -> return R.drawable.flag_swaziland
            "tc" //turks & caicos islands
            -> return R.drawable.flag_turks_and_caicos_islands
            "td" //chad
            -> return R.drawable.flag_chad
            "tg" //togo
            -> return R.drawable.flag_togo
            "th" //thailand
            -> return R.drawable.flag_thailand
            "tj" //tajikistan
            -> return R.drawable.flag_tajikistan
            "tk" //tokelau
            -> return R.drawable.flag_tokelau // custom
            "tl" //timor-leste
            -> return R.drawable.flag_timor_leste
            "tm" //turkmenistan
            -> return R.drawable.flag_turkmenistan
            "tn" //tunisia
            -> return R.drawable.flag_tunisia
            "to" //tonga
            -> return R.drawable.flag_tonga
            "tr" //turkey
            -> return R.drawable.flag_turkey
            "tt" //trinidad & tobago
            -> return R.drawable.flag_trinidad_and_tobago
            "tv" //tuvalu
            -> return R.drawable.flag_tuvalu
            "tw" //taiwan, province of china
            -> return R.drawable.flag_taiwan
            "tz" //tanzania, united republic of
            -> return R.drawable.flag_tanzania
            "ua" //ukraine
            -> return R.drawable.flag_ukraine
            "ug" //uganda
            -> return R.drawable.flag_uganda
            "us" //united states
            -> return R.drawable.flag_united_states_of_america
            "uy" //uruguay
            -> return R.drawable.flag_uruguay
            "uz" //uzbekistan
            -> return R.drawable.flag_uzbekistan
            "va" //holy see (vatican city state)
            -> return R.drawable.flag_vatican_city
            "vc" //st vincent & the grenadines
            -> return R.drawable.flag_saint_vicent_and_the_grenadines
            "ve" //venezuela, bolivarian republic of
            -> return R.drawable.flag_venezuela
            "vg" //british virgin islands
            -> return R.drawable.flag_british_virgin_islands
            "vi" //us virgin islands
            -> return R.drawable.flag_us_virgin_islands
            "vn" //vietnam
            -> return R.drawable.flag_vietnam
            "vu" //vanuatu
            -> return R.drawable.flag_vanuatu
            "wf" //wallis and futuna
            -> return R.drawable.flag_wallis_and_futuna
            "ws" //samoa
            -> return R.drawable.flag_samoa
            "xk" //kosovo
            -> return R.drawable.flag_kosovo
            "ye" //yemen
            -> return R.drawable.flag_yemen
            "yt" //mayotte
            -> return R.drawable.flag_martinique // no exact flag found
            "za" //south africa
            -> return R.drawable.flag_south_africa
            "zm" //zambia
            -> return R.drawable.flag_zambia
            "zw" //zimbabwe
            -> return R.drawable.flag_zimbabwe
            else -> return R.drawable.flag_transparent
        }
    }

    fun formatPhone(edt: EditText) {
        val text = edt.getText().toString()
        val textLength = edt.getText()!!.length
        if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" "))
            return
//        if (textLength == 2) {
//            edt.setText(StringBuilder(text).insert(text.length, " ").toString())
//            edt.setSelection(edt.getText()!!.length)
//        }
        if (textLength == 1) {
            if (!text.contains("(")) {
                edt.setText(StringBuilder(text).insert(text.length - 1, "(").toString())
                edt.setSelection(edt.getText()!!.length)
            }
        } else if (textLength == 5) {
            if (!text.contains(")")) {
                edt.setText(StringBuilder(text).insert(text.length - 1, ")").toString())
                edt.setSelection(edt.getText()!!.length)
            }
        } else if (textLength == 6) {
            edt.setText(StringBuilder(text).insert(text.length - 1, " ").toString())
            edt.setSelection(edt.getText()!!.length)
        } else if (textLength == 10) {
            if (!text.contains("-")) {
                edt.setText(StringBuilder(text).insert(text.length - 1, "-").toString())
                edt.setSelection(edt.getText()!!.length)
            }
        } else if (textLength == 15) {
            if (text.contains("-")) {
                edt.setText(StringBuilder(text).insert(text.length - 1, "-").toString())
                edt.setSelection(edt.getText()!!.length)
            }
        } else if (textLength == 18) {
            if (text.contains("-")) {
                edt.setText(StringBuilder(text).insert(text.length - 1, "-").toString())
                edt.setSelection(edt.getText()!!.length)
            }
        }
    }

    fun convertDpToPixel(context: Context, dp: Float): Int {
        val resources = context.resources
        val displayMetrics = resources.displayMetrics
        val pixels = TypedValue.applyDimension(1, dp, displayMetrics)
        return pixels.toInt()
    }

    fun convertActiveTrip(data: String): String{

        val first = data.substring(0, 4)
        val last = data.substring(data.length - 5, data.length)

        return first + "..." + last
    }

}