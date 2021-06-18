package org.pacific_emis.surveys.core.data.data_source;

import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Single<List<School>> loadSchools();

    Single<List<Teacher>> loadTeachers();

    Completable rewriteAllSchools(List<School> schools);

    Completable rewriteAllTeachers(List<Teacher> schools);

    Completable rewriteAllSubjects(List<Subject> subjects);

    Completable rewriteTemplateSurvey(Survey survey);

    Single<Survey> getTemplateSurvey();

    Single<Survey> loadSurvey(long surveyId);

    Single<List<Survey>> loadAllSurveys();

    Single<List<Survey>> loadSurveys(String schoolId, AppRegion appRegion, String surveyTag);

    Single<Survey> createSurvey(String schoolId, String schoolName, Date createDate, String surveyTag, String userEmail);

    Completable deleteSurvey(long surveyId);

    Single<Photo> createPhoto(Photo photo, long answerId);

    Completable deletePhoto(long photoId);

    Completable deleteCreatedSurveys();

    Completable createPartiallySavedSurvey(Survey survey);

    void updateSurvey(Survey survey);

    List<Photo> getPhotos(Survey survey);

    Completable updatePhotoWithRemote(Photo photo, String remoteFileId);
}
