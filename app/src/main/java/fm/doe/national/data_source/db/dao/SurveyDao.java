package fm.doe.national.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data_source.db.models.OrmLiteSchool;
import fm.doe.national.data_source.db.models.OrmLiteSurvey;
import io.reactivex.Single;

public class SurveyDao extends BaseRxDao<OrmLiteSurvey, Long> {

    SurveyDao(ConnectionSource connectionSource, Class<OrmLiteSurvey> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteSurvey> createSurvey(OrmLiteSchool school, int year) {
        return Single.fromCallable(() -> {
            OrmLiteSurvey survey = new OrmLiteSurvey(year, school);
            create(survey);
            return survey;
        });
    }


}
