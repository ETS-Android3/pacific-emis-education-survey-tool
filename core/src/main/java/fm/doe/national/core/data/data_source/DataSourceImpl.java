package fm.doe.national.core.data.data_source;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.persistence.SchoolsDatabase;
import fm.doe.national.core.data.persistence.dao.SchoolDao;
import fm.doe.national.core.data.persistence.model.RoomSchool;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.utils.CollectionUtils;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public abstract class DataSourceImpl implements DataSource {

    private static final String SCHOOLS_DATABASE_NAME = "schools.database";

    protected final SchoolsDatabase schoolsDatabase;

    protected final LocalSettings localSettings;

    protected final SchoolDao schoolDao;

    public DataSourceImpl(Context applicationContext, LocalSettings localSettings) {
        this.localSettings = localSettings;
        schoolsDatabase = Room.databaseBuilder(applicationContext, SchoolsDatabase.class, SCHOOLS_DATABASE_NAME).build();
        schoolDao = schoolsDatabase.getSchoolDao();
    }

    public void closeConnections() {
        schoolsDatabase.close();
    }

    @Override
    public Single<List<School>> loadSchools() {
        return Single.fromCallable(() -> schoolDao.getAll(localSettings.getAppRegion()))
                .map(ArrayList::new);
    }

    @Override
    public Completable rewriteAllSchools(List<School> schools) {
        if (CollectionUtils.isEmpty(schools)) {
            return Completable.complete();
        }

        final AppRegion appRegion = schools.get(0).getAppRegion();

        return Observable.fromIterable(schools)
                .map(RoomSchool::new)
                .toList()
                .flatMapCompletable(roomSchools -> Completable.fromAction(() -> {
                    schoolDao.deleteAllForAppRegion(appRegion);
                    schoolDao.insert(roomSchools);
                }));
    }

}
