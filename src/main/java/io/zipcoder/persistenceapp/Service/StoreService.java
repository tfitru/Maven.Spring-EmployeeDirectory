package io.zipcoder.persistenceapp.Service;

import io.zipcoder.persistenceapp.Model.Department;
import io.zipcoder.persistenceapp.Model.Employee;
import io.zipcoder.persistenceapp.Model.Manager;
import io.zipcoder.persistenceapp.Repo.DepartmentRepo;
import io.zipcoder.persistenceapp.Repo.EmployeRepo;
import io.zipcoder.persistenceapp.Repo.ManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class StoreService {

    private DepartmentRepo departmentRepo;
    private EmployeRepo employeRepo;
    private ManagerRepo managerRepo;

    @Autowired
    public StoreService(DepartmentRepo departmentRepo, EmployeRepo employeRepo, ManagerRepo managerRepo) {
        this.departmentRepo = departmentRepo;
        this.employeRepo = employeRepo;
        this.managerRepo = managerRepo;
    }

    public void save(Employee employee){
        employeRepo.save(employee);
    }

    public void save(Department department){
        departmentRepo.save(department);
    }

    public void save(Manager manager){
        managerRepo.save(manager);
    }

    public void setManager(Integer empId, Integer manId){
        Employee employee = employeRepo.findOne(empId);
        Manager manager = managerRepo.findOne(manId);
        employee.setManager(manager);
        manager.getEmployee().add(employee);
    }

    public Employee findEmp(Integer id){
        return employeRepo.findOne(id);
    }

    public Employee update(Employee oldEmployee, Employee employee){
        if(oldEmployee==null){
            employeRepo.save(employee);
            return employee;
        } else {
            oldEmployee.setFirst_name(employee.getFirst_name());
            oldEmployee.setLast_name(employee.getLast_name());
            oldEmployee.setEmail(employee.getEmail());
            oldEmployee.setTitle(employee.getTitle());
            oldEmployee.setPhone_numb(employee.getPhone_numb());
            oldEmployee.setHire_date(employee.getHire_date());
            employeRepo.save(oldEmployee);
        }
        return oldEmployee;
    }

    public Manager findMan(Integer id){
        return managerRepo.findOne(id);
    }

    public Department findDep(Integer id){
        return departmentRepo.findOne(id);
    }

    public Department updateDep(Integer manId, Integer depId){
        Manager manager = this.findMan(manId);
        Department department = this.findDep(depId);

        department.setManager(manager);
        manager.setDepartment(department);
        departmentRepo.save(department);
        managerRepo.save(manager);

        return department;
    }


    public Department updateDepName(Integer id, Department department){
        Department oldDepartment = this.findDep(id);
        oldDepartment.setName(department.getName());
        departmentRepo.save(department);
        return department;
    }

    public Set<Employee> employeesList(Integer id){
        Manager manager = this.findMan(id);
        return manager.getEmployee();
    }

    public Set<Employee> employeesNoManager(Integer id){
        Set<Employee> listNoManager = new HashSet<>();
        for(int i = 0; i<employeRepo.findAll().size(); i++){
            if(employeRepo.findOne(i).getManager()==null){
                listNoManager.add(employeRepo.findOne(i));
            }
        }
        return listNoManager;
    }

    public Employee setDep(Integer id, Integer depId){
        Employee employee = this.findEmp(id);
        Department department = this.findDep(depId);

        if(employee!=null && department!=null) {
            employee.setDepartment(department);
            employeRepo.save(employee);
            departmentRepo.save(department);
            return employee;
        }else {
            System.out.println("No employee or Department found");
            return employee;
        }
    }

    public Set<Employee> getAllEmployeesFromDep(Integer id){
        Set<Employee> allEmp = new HashSet<>();
        Department department = this.findDep(id);

        for(int i=0; i<employeRepo.findAll().size(); i++){
            if(employeRepo.findAll().get(i).getDepartment().equals(department)){
                allEmp.add(employeRepo.findAll().get(i));
            }
        }
        return allEmp;
    }

//    Get all employees who report directly or indirectly to a particular manager
//    This should still work for an employee who is not a manager -- they have no direct reports

    public Set<Employee> allEmployees(Integer id){
        Manager manager = this.findMan(id);
        Set<Employee> employees = new HashSet<>();
        for(int i = 0; i<employeRepo.findAll().size(); i++){
            if(employeRepo.findAll().get(i).getManager().equals(manager)){
                employees.add(employeRepo.findAll().get(i));
            }
        }
        for(int i = 0; i<departmentRepo.findAll().size(); i++){
            if(manager.getDepartment().equals(departmentRepo.findAll().get(i))){
                employees.addAll(departmentRepo.findAll().get(i).getEmployees());
            }
        }
        return employees;
    }


    public void deleteEmployee(Integer id, Integer manId, Integer depId){
        Employee employee = this.findEmp(id);
        Manager manager = this.findMan(manId);
        Department department = this.findDep(depId);

        if(employee==null || manager==null || department==null){
            System.out.println("Nothing found");
        } else {
            if (employee.getDepartment() != null || employee.getManager() != null) {
                employee.setDepartment(null);
                employee.setManager(null);
                manager.getEmployee().remove(employee);
                department.getEmployees().remove(employee);
                this.save(employee);
                this.save(manager);
                this.save(department);
            }
        }

        employeRepo.delete(employee);
    }

    public void deleteAllEmployeesByDepartment(Integer manId, Integer depId){
        Manager manager = this.findMan(manId);
        Department department = this.findDep(depId);

        if(manager==null || department==null){
            System.out.println("Nothing found");
        } else {
            for(int i=0; i<employeRepo.findAll().size(); i++){
                if(employeRepo.findAll().get(i).getDepartment()==department){
                    Employee employee = employeRepo.findAll().get(i);
                    if(employee.getManager()!=null){
                        employee.setManager(null);
                        employee.setDepartment(null);
                        employeRepo.save(employee);
                    } else{
                        employee.setDepartment(null);
                        employeRepo.save(employee);
                    }
                }
            }
            department.getEmployees().clear();
            departmentRepo.save(department);
            }
        }



    public String employeeTitle(Integer id){
        Employee employee = findEmp(id);
        return employee.getTitle();
    }

    public void deleteAllEmployeesByManager(Integer manId) {
        Manager manager = this.findMan(manId);
        for(int i= 0; i<employeRepo.findAll().size(); i++){
            if(employeRepo.findAll().get(i).getManager()==manager){
                Employee employee = employeRepo.findAll().get(i);
                employee.setManager(null);
                employeRepo.save(employee);
            }
        }
        for(int i =0 ; i<departmentRepo.findAll().size(); i++){
            if(departmentRepo.findAll().get(i).getManager()==manager){
                Department department = departmentRepo.findAll().get(i);
                department.setManager(null);
                departmentRepo.save(department);
            }
        }
        manager.getEmployee().clear();
        manager.setDepartment(null);
        managerRepo.save(manager);
        managerRepo.delete(manager);
    }

    public void deleteAllEmployeesByManagerIndirectReport(Integer manId) {
        Manager manager = this.findMan(manId);
        Manager newManager = managerRepo.findOne(manager.getId()+1);

        for(int i = 0; i<employeRepo.findAll().size(); i++){
            if(employeRepo.findAll().get(i).getManager().equals(manager)){
                Employee employee = employeRepo.findAll().get(i);
                employee.setManager(newManager);
                newManager.getEmployee().add(employee);
                employeRepo.save(employee);
                managerRepo.save(newManager);
            }
        }

        for(int i = 0; i<departmentRepo.findAll().size(); i++){
            if(departmentRepo.findAll().get(i).getManager().equals(manager)){
                Department department = departmentRepo.findAll().get(i);
                department.setManager(null);
                departmentRepo.save(department);
            }
        }

        manager.getEmployee().clear();
        manager.setDepartment(null);
        managerRepo.save(manager);
        managerRepo.delete(manager);

    }

    public Department departmentByName(String departmentName){
        Department department = new Department();
        for(int i = 0; i<departmentRepo.findAll().size(); i++){
            if(departmentRepo.findAll().get(i).getName().equalsIgnoreCase(departmentName)) {
                department = departmentRepo.findAll().get(i);
            }
        }
        return department;
    }

    public Department mergeDepartment(String departmentName, String oldDepartmentName) {
        Department A = this.departmentByName(departmentName);
        Department B = this.departmentByName(oldDepartmentName);


        A.getEmployees().addAll(B.getEmployees());

        for(int i = 0; i<employeRepo.findAll().size(); i++){
            if(employeRepo.findAll().get(i).getDepartment().equals(B)){
                Employee employee = employeRepo.findAll().get(i);
                employee.setDepartment(A);
                employeRepo.save(employee);
            }
        }
        for(int i = 0; i<managerRepo.findAll().size(); i++){
            if(managerRepo.findAll().get(i).getDepartment().equals(A)){
                Manager manager = managerRepo.findAll().get(i);
                manager.getManagers().add(B.getManager());
                managerRepo.save(manager);
            }
        }

        departmentRepo.save(A);
        departmentRepo.save(B);

        return A;
    }


//    Merge departments (given two department names eg: A and B, move the manager of B to report to the manager of A,
//    and update all other employees to be members of department A)



}
