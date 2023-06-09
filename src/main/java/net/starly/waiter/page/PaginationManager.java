package net.starly.waiter.page;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PaginationManager {

    @Getter
    private final List<WaiterPage> pages;
    @Getter private int currentPage;

    public PaginationManager(List<ItemStack> items) {
        this.pages = paginateItems(items);
        this.currentPage = 1;
    }

    public void nextPage() {
        if (currentPage < pages.size()) currentPage++;
    }

    public void prevPage() {
        if (currentPage > 1) currentPage--;
    }

    public WaiterPage getCurrentPageData() { return pages.get(currentPage - 1); }

    public List<WaiterPage> paginateItems(List<ItemStack> items) {
        List<WaiterPage> pages = new ArrayList<>();
        int itemCount = items.size();
        int pageCount = (int) Math.ceil((double) itemCount / 45);
        for (int i = 0; i < pageCount; i++) {
            int start = i * 45;
            int end = Math.min(start + 45, itemCount);
            List<ItemStack> pageItems = items.subList(start, end);
            pages.add(new WaiterPage(i + 1, pageItems));
        }
        return pages;
    }
}
