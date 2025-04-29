package com.nefarious.socialnetwork.auth.repository;

import com.nefarious.socialnetwork.auth.entity.OtpEntry;
import org.springframework.data.repository.CrudRepository;

public interface OtpRepository extends CrudRepository<OtpEntry, String> {
}
