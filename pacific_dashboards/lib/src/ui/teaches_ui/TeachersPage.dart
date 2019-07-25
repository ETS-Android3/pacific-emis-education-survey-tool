import 'package:flutter/material.dart';
import '../../config/Constants.dart';
import '../../models/TeacherModel.dart';
import '../../models/TeachersModel.dart';
import '../../blocs/TeachersBloc.dart';
import '../BaseTileWidget.dart';
import '../ChartFactory.dart';
import '../ChartInfoTable.dart';
import '../InfoTable.dart';
import '../PlatformAppBar.dart';
import '../TitleWidget.dart';

class TeachersPage extends StatefulWidget {
  static const String _kPageName = "Teachers";
  static const String _measureName = "Teachers";

  final TeachersBloc bloc;

  final Widget _dividerWidget = Divider(
    height: 16.0,
    color: Colors.white,
  );

  TeachersPage({
    Key key,
    this.bloc,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return TeachersPageState();
  }
}

class TeachersPageState extends State<TeachersPage> {
  @override
  void initState() {
    super.initState();
    widget.bloc.fetchData();
  }

  @override
  void dispose() {
    debugPrint("disposing");
    widget.bloc.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PlatformAppBar(
        iconTheme: new IconThemeData(color: AppColors.kWhite),
        backgroundColor: AppColors.kDenim,
        title: Text(
          TeachersPage._kPageName,
          style: TextStyle(
            color: AppColors.kWhite,
            fontSize: 18.0,
            fontFamily: "Noto Sans",
          ),
        ),
      ),
      body: StreamBuilder(
        stream: widget.bloc.data,
        builder: (context, AsyncSnapshot<TeachersModel> snapshot) {
          if (snapshot.hasData) {
            return Padding(
              padding: EdgeInsets.all(16.0),
              child: _buildList(snapshot),
            );
          } else if (snapshot.hasError) {
            return Text(snapshot.error.toString());
          }

          return Center(
            child: CircularProgressIndicator(),
          );
        },
      ),
    );
  }

  Widget _buildList(AsyncSnapshot<TeachersModel> snapshot) {
    return OrientationBuilder(
      builder: (context, orientation) {
        return ListView.builder(
          itemCount: 4,
          itemBuilder: (BuildContext context, int index) {
            return ListTile(
              contentPadding: EdgeInsets.symmetric(horizontal: 0.0),
              subtitle: _generateGridTile(snapshot.data, index),
            );
          },
        );
      },
    );
  }

  Widget _generateGridTile(TeachersModel data, int index) {
    switch (index) {
      case 0:
        return BaseTileWidget(
//            title: TitleWidget.withFilter("Teachers by Authority", AppColors.kRacingGreen, data.authorityFilter),
          title: TitleWidget("Teachers by Authority", AppColors.kRacingGreen),
          body: Column(
            children: <Widget>[
              ChartFactory.getPieChartViewByData(_generateMapOfSum(data.getSortedByAuthority())),
              widget._dividerWidget,
              ChartInfoTable<TeacherModel>(data.getSortedByAuthority().keys.toList(), _generateMapOfSum(data.getSortedByAuthority()),
                  "Authority", TeachersPage._measureName, data.authorityFilter.selectedKey),
            ],
          ),
        );
        break;
      case 1:
        return BaseTileWidget(
          title: TitleWidget("Schools Enrollment Govt / \nNon-govt", AppColors.kRacingGreen),
          body: Column(
            children: <Widget>[
              ChartFactory.getPieChartViewByData(_generateMapOfSum(data.getSortedByGovt())),
              widget._dividerWidget,
              ChartInfoTable<TeacherModel>(data.getSortedByGovt().keys.toList(), _generateMapOfSum(data.getSortedByGovt()),
                  "Public/Private", TeachersPage._measureName, data.stateFilter.selectedKey),
            ],
          ),
        );
        break;
      case 2:
        return BaseTileWidget(
//            title: TitleWidget.withFilter("Teachers by State", AppColors.kRacingGreen, data.stateFilter),
          title: TitleWidget("Teachers by State", AppColors.kRacingGreen),
          body: Column(
            children: <Widget>[
              ChartFactory.getBarChartViewByData(_generateMapOfSum(data.getSortedByState())),
              widget._dividerWidget,
              ChartInfoTable<TeacherModel>(data.getSortedByState().keys.toList(), _generateMapOfSum(data.getSortedByState()), "State",
                  TeachersPage._measureName, data.stateFilter.selectedKey),
            ],
          ),
        );
        break;
      default:
        var statesKeys = data.getDistrictCodeKeysList();
        List<Widget> widgets = List<Widget>();

        widgets.add(InfoTable<TeacherModel>(data.getSortedBySchoolType(), "Total", "School \nType"));

        for (var i = 0; i < statesKeys.length; ++i) {
          widgets.add(widget._dividerWidget);
          widgets.add(InfoTable<TeacherModel>.subTable(data.getSortedBySchoolType(), statesKeys[i], "School \nType"));
        }

        return BaseTileWidget(
//            title: TitleWidget.withFilter("Teachers by School type, State and \nGender", AppColors.kRacingGreen, data.schoolTypeFilter),
          title: TitleWidget("Teachers by School type, State and \nGender", AppColors.kRacingGreen),
          body: Column(
            children: widgets,
          ),
        );
        break;
    }
  }

  static Map<dynamic, int> _generateMapOfSum(Map<dynamic, List<TeacherModel>> listMap) {
    Map<dynamic, int> mapOfSum = new Map<dynamic, int>();
    int sum = 0;

    listMap.forEach((k, v) {
      sum = 0;

      listMap[k].forEach((school) {
        sum += school.numTeachersM + school.numTeachersF;
      });

      mapOfSum[k] = sum;
    });

    return mapOfSum;
  }
}
