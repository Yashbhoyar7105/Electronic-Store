package com.lcwd.electronicStore;

import com.lcwd.electronicStore.entity.Role;
import com.lcwd.electronicStore.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository repository;

    @Value("${normal.role.id}")
    private String normal_role_id;

    @Value("${admin.role.id}")
    private String admin_role_id;

    @Override
    public void run(String... args) throws Exception {

        System.out.println(passwordEncoder.encode("abcd"));

       try{
           Role roleAdmin = Role.builder().roleId(admin_role_id).roleName("Role_Admin").build();
           Role roleNormal = Role.builder().roleId(normal_role_id).roleName("Role_Normal").build();
           repository.save(roleAdmin);
           repository.save(roleNormal);

       } catch (Exception e) {
           e.printStackTrace();
       }

    }
}
