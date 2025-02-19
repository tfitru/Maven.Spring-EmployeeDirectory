package io.zipcoder.persistenceapp.Controller;


import io.zipcoder.persistenceapp.Model.Department;
import io.zipcoder.persistenceapp.Model.Employee;
import io.zipcoder.persistenceapp.Model.Manager;
import io.zipcoder.persistenceapp.Service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }


    @PostMapping("/emp")
    public int saveEmp(@Valid @RequestBody Employee employee){
        storeService.save(employee);
        return employee.getId();
    }

    @PostMapping("/dep")
    public int saveDep(@Valid @RequestBody Department department){
        storeService.save(department);
        return department.getId();
    }

    @PostMapping("/man")
    public int saveEmp(@Valid @RequestBody Manager manager){
        storeService.save(manager);
        return manager.getId();
    }

    @PutMapping("/{id}/{empid}")
    public Employee updateManager( @PathVariable("id") Integer id, @PathVariable("empid") Integer empid){
        storeService.setManager(id, empid);
        return storeService.findEmp(id);
    }

    @PutMapping("/emp/{id}")
    public Employee update(@PathVariable("id") Integer id, @RequestBody Employee employee){
        Employee oldEmp = storeService.findEmp(id);
        return storeService.update(oldEmp, employee);
    }

    @PutMapping("/dep/{id}/{manId}")
    public Department updateDep(@PathVariable("id") Integer id, @PathVariable("manId") Integer manId ){
        return storeService.updateDep(manId, id);
    }

    @PutMapping("/dep/{id}")
    public Department updateDepName(@PathVariable("id") Integer id, @RequestBody Department department){
        return storeService.updateDepName(id, department);
    }

    @PutMapping("/emp/{id}/{depId}")
    public Employee setDep(@PathVariable("id") Integer id, @PathVariable("depId") Integer depId){
        return storeService.setDep(id, depId);
    }

    @GetMapping("/man/{id}")
    public Set<Employee> listEmployees(@PathVariable("id") Integer id){
        return storeService.employeesList(id);
    }

    @GetMapping("/emp/noMan/{id}")
    public Set<Employee> listEmployeesNoManager(@PathVariable("id") Integer id) {
        return storeService.employeesNoManager(id);
    }

//    Get all employees who report directly or indirectly to a particular manager
//    This should still work for an employee who is not a manager -- they have no direct reports
    @GetMapping("/emp/allemp/{id}")
    public Set<Employee> listEmployeesReport(@PathVariable("id") Integer id){
        return storeService.allEmployees(id);
    }

    @GetMapping("/emp/{id}/title")
    public String employeeTitle(@PathVariable("id") Integer id){
        return storeService.employeeTitle(id);
    }

    @DeleteMapping("/emp/{id}/{manId}/{depId}")
    public void deleteEmployee(@PathVariable("id") Integer id, @PathVariable("manId")
    Integer manId, @PathVariable("depId") Integer depId){
        storeService.deleteEmployee(id, manId, depId);
    }


    @DeleteMapping("/emp/{manId}/depId")
    public void deleteAllEmployeesByDepartment(@PathVariable("manId") Integer manId, @PathVariable("depId") Integer depId){
        storeService.deleteAllEmployeesByDepartment(manId,depId);
    }


    //    Remove all employees under a particular manager (Including indirect reports)
    @DeleteMapping("/emp/{manId}")
    public void deleteAllEmployeesByManager(@PathVariable("manId") Integer manId){
        storeService.deleteAllEmployeesByManager(manId);
    }

    @DeleteMapping("/emp/{manId}/next")
    public void deleteAllEmployeesByManagerNext(@PathVariable("manId") Integer manId){
        storeService.deleteAllEmployeesByManagerIndirectReport(manId);
    }

    @PutMapping("/dep/{departmentName}/{oldDepartmentName}")
    public Department departmentMerge(@PathVariable("departmentName") String departmentName, @PathVariable("oldDepartmentName") String oldDepartmentName){
        return storeService.mergeDepartment(departmentName, oldDepartmentName);
    }

}
