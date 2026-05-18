package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.programdoo.transport.R;
import com.programdoo.transport.ui.adapters.selectionModel.SelectionEngine;
import com.programdoo.transport.ui.callbacks.OnItemClickListener;
import com.programdoo.transport.ui.callbacks.OnItemLongClickListener;
import com.programdoo.transport.ui.callbacks.OnSelectionChangedListener;
import com.programdoo.transport.utils.EntityToOptionMapper;
import com.programdoo.transport.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

/**
 *
 * @param <T> tip entity-ja koji se prikazuje u adapteru
 * @param <VH> tip ViewHoldera koji ce da drzi podatke u adapteru
 * <p>
 *            recycler view adapter definise kako se podaci prikazuju unutar recycler view-a.<br>
 *            recycler view je ui komponenta koja omogucava cuvanje i prikazivanje podataka, s tim
 *            sto ostavlja programeru da definise kako ce to da izvede putem adaptera. cesto se
 *            koristi za prikaz liste.
 *            BaseRecyclerViewAdapter sadrzi genericku logiku koju primenjuje svaki recycler view
 *            adapter. omogucava filtriranje na osnovu keyword-a po opisu svakog itema. opis itema
 *            se definise <b>mapper</b>-om u konkretnoj implementaciji GenericRecyclerViewAdapter-a. <br>
 *
 * </p>
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends BaseRecyclerViewAdapter.BaseViewHolder>
        extends RecyclerView.Adapter<VH> implements Filterable {
    protected List<T> allItems;
    protected final List<T> displayedItems;
    protected final SelectionEngine selectionEngine = new SelectionEngine();
    protected OnItemClickListener<T> itemClickListener;
    protected OnItemLongClickListener<T> itemLongClickListener;
    @Getter
    protected EntityToOptionMapper<T> mapper;
    protected Filter keywordFilter;
    private OnSelectionChangedListener selectionChangedListener;
    private @ColorInt int unselectedIconTint;
    private @ColorInt int selectedIconTint;

    /**
     *
     * @param items lista item-a koje treba prikazati. moze da se inicijalizuje praznom listom
     *              <p>
     *                  u konstruktoru konkretne implementacije ove klase potrebno je implementirati
     *                  <b>EntityToOptionMapper</b> interfejs. Ukoliko postoje "skriveni" podaci preko
     *                  kojih se radi filtriranje, potrebno je override-ovati i getSearchData metod.
     *              </p>
     */
    public BaseRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<T> items) {
        this.allItems = items;
        this.displayedItems = items;

        keywordFilter = createKeywordFilter();

        unselectedIconTint = ContextCompat.getColor(context, R.color.tertiaryLighter);
        selectedIconTint = ContextCompat.getColor(context, R.color.primaryLighter);
    }

    public BaseRecyclerViewAdapter(
            @NonNull Context context,
            @NonNull List<T> items,
            @ColorRes int unselectedIconTint,
            @ColorRes int selectedIconTint) {
        this.allItems = items;
        this.displayedItems = items;

        keywordFilter = createKeywordFilter();

        this.unselectedIconTint = ContextCompat.getColor(context, unselectedIconTint);
        this.selectedIconTint = ContextCompat.getColor(context, selectedIconTint);
    }
    /**
     *
     * @param data
     * <p>
     *     koristi se iskljucivo za postavljanje potpuno novih podataka (na primer, ako stizu sa APIja)
     * </p>
     */
    public void setData(List<T> data) {
        allItems.clear();
        allItems.addAll(data);
        displayedItems.clear();
        displayedItems.addAll(data);
        notifyDataSetChanged();

        tryResolvePendingSelection();
    }

    public void updateDisplayed(List<T> newData) {
        ArrayList<T> allItemsSave = new ArrayList<>(allItems);
        displayedItems.clear();
        displayedItems.addAll(newData);
        HashSet<Integer> displayedIds = displayedItems.stream()
                .map(mapper::getId)
                .collect(Collectors.toCollection(HashSet::new));
        boolean changed = selectionEngine.retainOnly(displayedIds);
        if (changed && selectionChangedListener != null) {
            selectionChangedListener.onSelectionChanged(new ArrayList<>(
                    selectionEngine.getDescriptions()
            ));
        }

        notifyDataSetChanged();
        allItems = allItemsSave;
    }

    public void setUnselectedIconTint(
            @NonNull Context context,
            @ColorRes int unselected) {
        this.unselectedIconTint = ContextCompat.getColor(context, unselected);
        notifyDataSetChanged();
    }
    public void setIconTint(
            @NonNull Context context,
            @ColorRes int unselected,
            @ColorRes int selected) {
        this.unselectedIconTint = ContextCompat.getColor(context, unselected);
        this.selectedIconTint = ContextCompat.getColor(context, selected);
        // ovo moze da se optimizuje sa nekim PAYLOAD_ICON_TINT
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return displayedItems == null ? 0 : displayedItems.size();
    }

    public T getItemAt(int position) {
        return displayedItems.get(position);
    }

    public int getSelectedItemId(int position) {
        return mapper.getId(displayedItems.get(position));
    }

    public String getSelectedItemDescription(int position) {
        return mapper.getDescription(displayedItems.get(position));
    }

    public void setOnClickListener(OnItemClickListener<T> listener) {
        this.itemClickListener = listener;
    }

    public void setOnLongClickListener(OnItemLongClickListener<T> listener) {
        this.itemLongClickListener = listener;
    }
    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionChangedListener = listener;
    }

    @Override
    public Filter getFilter() {
        return keywordFilter;
    }

    public boolean isSelected() {
        return selectionEngine.isSelected();
    }
    public boolean isSelectedId(int id) {
        return selectionEngine.isSelectedId(id);
    }
    public List<Integer> getSelectedIds() {
        return selectionEngine.getIds();
    }
    public Integer getSelectedId() {
        ArrayList<Integer> ids = (ArrayList<Integer>) selectionEngine.getIds();
        return !ids.isEmpty() ? ids.get(0) : null;
    }

    public void addSelected(int id, String description) {
        boolean added = selectionEngine.add(id, description);
        if (added) emitDataChanged();
    }

    /**
     * pending selection se dodaje samo ako nisi siguran da item postoji u listi.
     * primer koriscenja: sacuvas novog vezbaca iz edit forme i posaljes sacuvan
     * id nekom drugom fragmentu preko fragment result. drugi fragment automatski treba
     * da selektuje novog vezbaca u nekom SelectDialog-u; medjutim, nakon cuvanja, treba
     * vremena da se sacuva nov vezbac i refresh-uje data stream (pa tek onda select list),
     * tako da ne mozes biti siguran da li on vec postoji u listi.
     * @param id id entity-ja
     */
    public void addPendingSelect(int id) {
        selectionEngine.addPendingSelect(id);
        tryResolvePendingSelection();
    }
    public void addSelected(List<Integer> ids, List<String> descriptions) {
        boolean added = selectionEngine.addMany(ids, descriptions);
        if (added) emitDataChanged();
    }
    public void toggleSelected(int id, String description, boolean clearCurrentSelection) {
        boolean changed = selectionEngine.toggle(id, description, clearCurrentSelection);
        if (changed) emitDataChanged();
    }
    public void clearSelection() {
        boolean changed = selectionEngine.clear();
        if (changed) emitDataChanged();
    }

    private void emitDataChanged() {
        if (selectionChangedListener != null)
            selectionChangedListener.onSelectionChanged(
                    selectionEngine.getDescriptions());
        notifyDataSetChanged();
    }

    /**
     * pending selection moze da se razresi u dva momenta:
     * - kad se adapter napuni novim podacima i, medju ostalim, doda se item ciji select se ceka
     * - ako adapter vec sadrzi podatak, kad se prvi put doda pending selection, moze odmah biti resen
     * pogledaj addPendingSelect.
     */
    private void tryResolvePendingSelection() {
        boolean changed = selectionEngine.tryResolvePendingSelect(
                displayedItems.stream().map(mapper::getId).collect(Collectors.toSet()),
                mapper,
                displayedItems);
        if (changed)
            emitDataChanged();
    }

    private Filter createKeywordFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence filter) {
                List<T> filtered = new ArrayList<>();
                String query = filter != null ? filter.toString().toLowerCase().replace(" ", "") : "";

                if (StringUtil.isNullOrEmpty(query)) {
                    filtered.addAll(allItems);
                }
                else {
                    for (T item: allItems) {
                        String searchData = mapper.getSearchData(item);
                        if (searchData.contains(query)) {
                            filtered.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                List<T> allItemsSave = new ArrayList<>(allItems);
                displayedItems.clear();
                displayedItems.addAll((List<T>) results.values);
                notifyDataSetChanged();
                /* allItems sad drzi referencu na allItemsSave. ukoliko nista drugo ne drzi
                 * referencu na allItems, bice garbage collected. posto je ovo private polje u ovoj
                 * klasi i prisupa mu se direktno samo odavde, nista drugo nece drzati ref. na stare
                 * podatke. mora da se koristi bas ovako i nakon notifyDataSetChanged, u suprotnom
                 * filtriranje nece raditi jer ce misliti da su rezultat filtera svi itemi ili
                 * ce da overwrite-uje ovu varijablu i obrise item-e skrivene filterom. */
                allItems = allItemsSave;
            }
        };
    }

    @Override
    public void onBindViewHolder(
            @NonNull VH holder, int position) {
        T item = displayedItems.get(position);
        holder.setIconTint(unselectedIconTint, selectedIconTint);
        holder.setSelectedVisual(isSelectedId(mapper.getId(item)));
    }

    class BaseViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

        private List<TextView> tintableTextViews;
        protected String unknownLabel = itemView.getContext().getString(R.string.label_unknown);
        private @ColorInt int unselectedIconTint;
        private @ColorInt int selectedIconTint;
        protected @ColorInt int unselectedColor;
        protected @ColorInt int selectedColor;
        BaseViewHolder(View view) {
            super(view);
            Drawable ogBgr = itemView.findViewById(R.id.root).getBackground();
            if (ogBgr instanceof ColorDrawable) unselectedColor = ((ColorDrawable) ogBgr).getColor();

            selectedColor = ContextCompat.getColor(itemView.getContext(), R.color.primaryLightest);
            collectTintableTextViews(view);
        }

        BaseViewHolder(
                View view,
                @ColorRes int selectedColor,
                @ColorRes int selectedIconTint) {
            super(view);
            this.selectedColor = ContextCompat.getColor(itemView.getContext(), selectedColor);
            Drawable ogBgr = itemView.findViewById(R.id.root).getBackground();
            if (ogBgr instanceof ColorDrawable) unselectedColor = ((ColorDrawable) ogBgr).getColor();

            this.selectedIconTint = ContextCompat.getColor(itemView.getContext(), selectedIconTint);

            collectTintableTextViews(view);
        }

        public void setSelectedVisual(boolean isSelected) {
            View root = itemView.findViewById(R.id.root);
            root.setBackgroundColor(isSelected
                    ? selectedColor
                    : unselectedColor);

            for (TextView tv: tintableTextViews) {
                Drawable[] drawables = TextViewCompat.getCompoundDrawablesRelative(tv);
                for (Drawable d: drawables) {
                    if (d == null) continue;

                    d = d.mutate();
                    DrawableCompat.setTint(d, isSelected ? selectedIconTint : unselectedIconTint);
                }
            }
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getBindingAdapterPosition(), displayedItems.get(getLayoutPosition()));
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(view, getBindingAdapterPosition(), displayedItems.get(getLayoutPosition()));
            }

            return true;
        }

        private void collectTintableTextViews(View root) {
            tintableTextViews = new ArrayList<>();
            collectRecursively(root);
        }
        private void collectRecursively(View view) {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                Drawable[] drawables = TextViewCompat.getCompoundDrawablesRelative(tv);
                for (Drawable d: drawables) {
                    if (d != null) {
                        tintableTextViews.add(tv);
                        break;
                    }
                }
            }

            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                for (int i = 0; i < vg.getChildCount(); ++i)
                    collectRecursively(vg.getChildAt(i));
            }
        }
        public void setIconTint(
                @ColorInt int unselected,
                @ColorInt int selected) {
            this.unselectedIconTint = unselected;
            this.selectedIconTint = selected;
        }
    }
}
