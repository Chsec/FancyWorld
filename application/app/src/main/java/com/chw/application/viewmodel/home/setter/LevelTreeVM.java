package com.chw.application.viewmodel.home.setter;

import com.chw.application.model.table.TreeTemplate;
import com.chw.application.model.repository.TreeTemplateRepo;
import com.chw.application.viewmodel.BaseVM;

import java.util.ArrayList;
import java.util.List;

public class LevelTreeVM extends BaseVM {

    private final ArrayList<TreeTemplate> oriGroup = new ArrayList<>();

    public LevelTreeVM() {

    }

    public List<TreeTemplate> getGroup(String groupName) {
//        List<TreeTemplate> group = TreeTemplateRepo.getGroup(groupName);
//        if (group != null && !group.isEmpty()) {
//            oriGroup.addAll(group);
//        }
        TreeTemplate treeTemplate = new TreeTemplate();
        treeTemplate.setNodeId(-1);
        oriGroup.add(treeTemplate);
        return oriGroup;
    }

    /**
     * 创建RecyclerView可用的节点
     *
     * @param isRoot     是否为根节点
     * @param nodeParent 父节点id
     * @param nodeDepth  节点深度
     * @return 默认节点
     */
    public TreeTemplate createNodeData(boolean isRoot, int nodeParent, int nodeDepth) {
        return new TreeTemplate(-1, "", isRoot, nodeParent, nodeDepth, 0, true, "");
    }

    /**
     * 保存数据
     *
     * @param datas     需要入库的数据集合
     * @param groupName 树的组名称
     */
    public void save(List<TreeTemplate> datas, String groupName) {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setNodeOrder(i);
        }
        TreeTemplateRepo.create(datas);
    }

}
