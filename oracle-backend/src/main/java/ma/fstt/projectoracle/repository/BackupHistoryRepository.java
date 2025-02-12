package ma.fstt.projectoracle.repository;


import  ma.fstt.projectoracle.entity.BackupHistory ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {
}
