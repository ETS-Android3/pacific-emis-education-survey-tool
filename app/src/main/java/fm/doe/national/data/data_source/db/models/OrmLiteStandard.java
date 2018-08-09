package fm.doe.national.data.data_source.db.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import fm.doe.national.models.survey.Standard;

@DatabaseTable
public class OrmLiteStandard {

    public interface Column {
        String ID = "id";
        String NAME = "name";
        String GROUP_STANDARDS = "groupStandard";
        String CRITERIAS = "criterias";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.NAME)
    protected String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.GROUP_STANDARDS)
    protected OrmLiteGroupStandard groupStandard;

    @SerializedName("criteria")
    @ForeignCollectionField(eager = true, columnName = Column.CRITERIAS)
    protected Collection<OrmLiteCriteria> criterias;

    public OrmLiteStandard() {
    }

    public OrmLiteStandard(String name, OrmLiteGroupStandard groupStandard) {
        this.name = name;
        this.groupStandard = groupStandard;
    }

    public long getId() {
        return id;
    }

    public long getGroupStandardId() {
        return groupStandard.getId();
    }

    @NonNull
    public String getName() {
        return name;
    }

    public Collection<OrmLiteCriteria> getCriterias() {
        return criterias;
    }

    public void setGroupStandard(OrmLiteGroupStandard groupStandard) {
        this.groupStandard = groupStandard;
    }
}

