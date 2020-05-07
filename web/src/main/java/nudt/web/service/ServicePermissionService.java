package nudt.web.service;

import nudt.web.entity.ServicePermission;

import java.util.List;

public interface ServicePermissionService {

    //保存权限和
    public ServicePermission save(ServicePermission servicePermission);

    public List<Integer> findPermissionIdsByServiceIDs(List<Integer> serviceids);
}
