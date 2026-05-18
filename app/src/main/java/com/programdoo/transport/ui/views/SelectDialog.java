package com.programdoo.transport.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.programdoo.transport.data.models.enums.BaseEnum;
import com.programdoo.transport.utils.StringUtil;

import java.util.List;

import javax.annotation.Nullable;

public class SelectDialog extends SelectBase {
    public SelectDialog(
            Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Integer getSelectedId() {
        return this.adapter.getSelectedId();
    }

    @Override
    protected void updateDescription(List<String> descriptionList) {
        if (!descriptionList.isEmpty())
            this.et.setText(descriptionList.get(0));
        else
            this.et.setText("");
    }

    /**
     *
     * @param id id entity-ja
     * @param description description itema
     *                    <p>
     *                      postavlja selektovan item. koristi se iskljucivo prilikom inicijalizacije
     *                      bindinga u fragmentu. <br>
     *                      za postavljanje selektovanog itema na bilo koji drugi nacin, koristi <b>setSelected(int position)</b>
     *                    </p>
     */
    public void setSelected(Integer id, String description) {
        if (id != null && !StringUtil.isNullOrEmpty(description)) {
            adapter.addSelected(id, description);
        }
    }



    public void setSelected(BaseEnum item) {
        if (item == null) return;
        adapter.addSelected(item.getValue(), item.getDescription());
    }
}
