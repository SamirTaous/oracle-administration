package ma.fstt.projectoracle.repository;

import ma.fstt.projectoracle.entity.BackupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupScheduleRepository extends JpaRepository<BackupSchedule, Long> {
}