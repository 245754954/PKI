package nudt.web.service.impl;

import nudt.web.entity.Staff;
import nudt.web.repository.StaffRepository;
import nudt.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    StaffRepository staffRepository;

    @Override
    @Transactional
    public Staff save(Staff staff) {
        Staff save = staffRepository.save(staff);
        return save;

    }

    @Override
    public Staff findStaffByUsername(String username) {
        return staffRepository.findStaffByUsername(username);
    }

    @Override
    public List<Staff> findStaffByCode(Integer code) {
        return staffRepository.findStaffByCode(code);
    }

    @Override
    public Staff findStaffById(Integer id) {
        return staffRepository.findStaffById(id);
    }

    @Override
    @Transactional
    public void deleteStaffById(Integer id) {
        staffRepository.deleteStaffById(id);
    }

    @Override
    public Page<Staff> findAll(Pageable pageable) {
        return staffRepository.findAll(pageable);
    }

    @Override
    public long countAll() {
        return staffRepository.count();
    }

    @Override
    public List<Staff> findStaffByUsernameContains(String username) {
        return staffRepository.findStaffByUsernameContains(username);
    }

    @Override
    @Transactional
    public void deleteByIdIn(List<Integer> ids) {
        staffRepository.deleteByIdIn(ids);
    }

    @Override
    public List<Staff> findAllByIdIn(List<Integer> ids) {
        return staffRepository.findAllByIdIn(ids);
    }


}
