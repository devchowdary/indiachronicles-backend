package com.dev.projecttesting.repository;

import com.dev.projecttesting.model.ContactInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {
}
