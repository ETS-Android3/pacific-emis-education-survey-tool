package fm.doe.national.data.persistence.new_model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public interface Survey extends IdentifiedObject {

    int getVersion();

    @NonNull
    SurveyType getSurveyType();

    @NonNull
    Date getDate();

    @NonNull
    School getSchool();

    @NonNull
    List<Category> getCategories();

}
