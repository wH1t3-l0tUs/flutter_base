import 'package:flutter/material.dart';
import 'package:flutter_screenutil/screenutil.dart';
import 'package:flutter_svg/svg.dart';
import 'package:flutterbloc/config/global.dart';
import 'package:flutterbloc/page/overlay/news_for_you/someone_makes_confirm.dart';

class NewsForYouMainPage extends StatefulWidget {
  @override
  _NewsForYouMainPageState createState() => _NewsForYouMainPageState();
}

class _NewsForYouMainPageState extends State<NewsForYouMainPage> {
  bool firstTime = true;

  @override
  Widget build(BuildContext context) {
    ScreenUtil.init(context, width: 414, height: 896, allowFontScaling: true);
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
      backgroundColor: Colors.black,
      body: Container(
        width: width,
        height: height,
        child: Stack(
          children: [
            Padding(
              padding: EdgeInsets.only(left: width * 0.121),
              child: buildBody(firstTime, width, height),
            ),
            Positioned(
              bottom: height * 0.1,
              left: 0,
              right: 0,
              child: Center(
                child: IconButton(
                  onPressed: () {
                    if (firstTime) {
                      setState(() {
                        firstTime = false;
                      });
                    }
                  },
                  iconSize: 40,
                  icon: SvgPicture.asset(
                    'assets/icons/OK.svg',
                    // width: width * 13.3 / 100,
                    // height: height * 5.1 / 100,
                    fit: BoxFit.cover,
                    color: Colors.white,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget buildBody(bool firstTime, double width, double height) {
    return firstTime
        ? Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(
                height: height * 0.356,
              ),
              Text("News\nfor you", style: AppTextStyle.h1)
            ],
          )
        : SomeoneMakesConfirmPage();
  }
}
