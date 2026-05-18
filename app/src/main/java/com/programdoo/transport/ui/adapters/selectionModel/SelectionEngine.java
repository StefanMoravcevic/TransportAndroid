package com.programdoo.transport.ui.adapters.selectionModel;

import com.programdoo.transport.utils.EntityToOptionMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class SelectionEngine {
    private final Map<Integer, SelectOptionItem> items = new HashMap<>();
    private Integer pendingSelectId = null;

    public void addPendingSelect(int id) {
        pendingSelectId = id;
    }

    public <T> boolean tryResolvePendingSelect(
            Set<Integer> validIds,
            EntityToOptionMapper<T> mapper,
            List<T> itemSource) {
        if (pendingSelectId == null)
            return false;

        if (validIds.contains(pendingSelectId)) {
            for (T item: itemSource) {
                if (mapper.getId(item) == pendingSelectId) {
                    items.put(
                            pendingSelectId,
                            new SelectOptionItem(pendingSelectId, mapper.getDescription(item)));
                    pendingSelectId = null;
                    return true;
                }
            }
        }

        return false;
    }
    public boolean isSelected() {
        return !items.isEmpty();
    }
    public boolean isSelectedId(int id) {
        return items.containsKey(id);
    }
    public boolean add(int id, String description) {
        return items.putIfAbsent(id, new SelectOptionItem(id, description)) == null;
    }
    public boolean addMany(List<Integer> ids, List<String> descriptions) {
        int before = items.size();
        for (int i = 0; i < ids.size(); ++i)
            add(ids.get(i), descriptions.get(i));

        return items.size() != before;
    }
    public boolean toggle(int id, String description, boolean clearCurrentSelection) {
        if (!items.containsKey(id)) {
            if (clearCurrentSelection)
                items.clear();
            return add(id, description);
        }
        else {
            return remove(id);
        }
    }
    public boolean remove(int id) {
        return items.remove(id) != null;
    }
    public boolean clear() {
        if (items.isEmpty())
            return false;
        items.clear();
        return true;
    }
    public boolean retainOnly(Set<Integer> validIds) {
        int before = items.size();
        items.keySet().removeIf(id -> !validIds.contains(id));
        return items.size() != before;
    }
    public List<String> getDescriptions() {
        return items.values().stream()
                .map(SelectOptionItem::getDescription)
                .collect(Collectors.toList());
    }
    public List<Integer> getIds() {
        return new ArrayList<>(items.keySet());
    }
}
