package fm.doe.national.data_source.db.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.models.SynchronizePlatform;
import fm.doe.national.models.survey.Answer;

@DatabaseTable
public class OrmLiteAnswer implements Answer {

    public interface Column {
        String ID = "id";
        String ANSWER = "answer";
        String SYNCHRONIZED_PLATFORMS = "synchronizePlatforms";
        String SUB_CRITERIA = "subCriteria";
        String SURVEY = "survey";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;
    @DatabaseField(columnName = Column.ANSWER)
    protected boolean answer;
    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = Column.SYNCHRONIZED_PLATFORMS)
    protected List<SynchronizePlatform> synchronizePlatforms;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SUB_CRITERIA)
    protected OrmLiteSubCriteria subCriteria;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY)
    protected OrmLiteSurvey survey;

    public OrmLiteAnswer() {
    }

    public OrmLiteAnswer(boolean answer, OrmLiteSubCriteria subCriteria, OrmLiteSurvey survey) {
        this.answer = answer;
        this.subCriteria = subCriteria;
        this.survey = survey;
        this.synchronizePlatforms = new ArrayList<>();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean getAnswer() {
        return answer;
    }

    @Override
    public void setAnswer(boolean answer) {
        if (this.answer != answer) {
            this.answer = answer;
            synchronizePlatforms.clear();
        }
    }

    @Override
    public List<SynchronizePlatform> getSynchronizedPlatforms() {
        return synchronizePlatforms;
    }

    @Override
    public void addSynchronizedPlatform(SynchronizePlatform platform) {
        if (!synchronizePlatforms.contains(platform)) {
            synchronizePlatforms.add(platform);
        }
    }

}
