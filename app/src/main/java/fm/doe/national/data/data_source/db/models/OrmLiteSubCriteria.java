package fm.doe.national.data.data_source.db.models;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fm.doe.national.models.survey.SubCriteria;

@DatabaseTable
public class OrmLiteSubCriteria {

    public interface Column {
        String ID = "id";
        String NAME = "name";
        String CRITERIA = "criteria";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.NAME)
    protected String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.CRITERIA)
    protected OrmLiteCriteria criteria;

    public OrmLiteSubCriteria() {
    }

    public OrmLiteSubCriteria(String name, OrmLiteCriteria criteria) {
        this.name = name;
        this.criteria = criteria;
    }

    public long getId() {
        return id;
    }

    public long getCriteriaId() {
        return criteria.getId();
    }

    public void setCriteria(OrmLiteCriteria criteria) {
        this.criteria = criteria;
    }

    @NonNull
    public String getName() {
        return name;
    }

}
