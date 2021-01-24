import 'package:flutter/material.dart';
import 'package:flutter_screenutil/screenutil.dart';

class SomeoneMakesConfirmPage extends StatefulWidget {
  @override
  _SomeoneMakesConfirmPageState createState() =>
      _SomeoneMakesConfirmPageState();
}

class _SomeoneMakesConfirmPageState extends State<SomeoneMakesConfirmPage> {
  @override
  Widget build(BuildContext context) {
    double height = MediaQuery.of(context).size.height;
    double width = MediaQuery.of(context).size.width;
    ScreenUtil.init(context, width: 414, height: 896, allowFontScaling: true);
    return Column(
      children: [
        SizedBox(
          height: height * 0.145,
        ),
        Image.asset(
          'assets/icons/img_avatar-default.png',
          width: width * 0.176,
          height: height * 0.085,
        ),
      ],
    );
  }
}
