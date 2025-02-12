import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/oracle';
const API_BASE_URL2 = 'http://localhost:8080/backup';
const API_BASE_URL3 = 'http://localhost:8080/api'
const API_BASE_URL4 = 'http://localhost:8080/audit'

export const fetchUsers = async () => {
  const response = await axios.get(`${API_BASE_URL}/users`);
  return response.data;
};

export const createUser = async (username, password, role, quota, tablespace) => {
  const response = await axios.post(`${API_BASE_URL}/users`, null, {
    params: { username, password, role, quota, tablespace },
  });
  return response.data;
};

export const deleteUser = async (username) => {
  const response = await axios.delete(`${API_BASE_URL}/users/${username}`);
  return response.data;
};

// Trigger manual backup
export const triggerManualBackup = async (backupType) => {
  const response = await axios.post(`${API_BASE_URL2}/manual`, null, {
    params: { backupType },
  });
  return response.data;
};

// Restore backup
export const restoreBackup = async () => {
  const response = await axios.post(`${API_BASE_URL2}/restore`);
  return response.data;
};

// Restore backup from a specific SCN
export const restoreBackupFromSCN = async (scn) => {
  const response = await axios.post(`${API_BASE_URL2}/restore/scn`, null, {
    params: { scn },
  });
  return response.data;
};

// Restore backup from a specific date
export const restoreBackupFromDate = async (date) => {
  const response = await axios.post(`${API_BASE_URL2}/restore/date`, null, {
    params: { date },
  });
  return response.data;
};

// Get backup history
export const fetchBackups = async () => {
  const response = await axios.get(`${API_BASE_URL2}/list`);
  return response.data;
};

// Get available SCNs for backup restoration
export const fetchBackupSCNs = async () => {
  const response = await axios.get(`${API_BASE_URL2}/scns`);
  return response.data;
};

// Schedule a backup
export const scheduleBackup = async (cronExpression) => {
  const response = await axios.post(`${API_BASE_URL2}/schedule`, null, {
    params: { cronExpression },
  });
  return response.data;
};

// Fetch AWR Report
export const fetchAwrReport = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL3}/awr-report`);
    return response.data; // Returns the list of DatabaseMetric objects
  } catch (error) {
    console.error('Error fetching AWR Report:', error);
    throw error;
  }
};

// Fetch ASH Report
export const fetchAshReport = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL3}/ash-report`);
    return response.data; // Returns the list of DatabaseMetric objects
  } catch (error) {
    console.error('Error fetching ASH Report:', error);
    throw error;
  }
};

// Fetch Real-Time Stats
export const fetchRealTimeStats = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL3}/real-time-stats`);
    return response.data; // Returns the list of DatabaseMetric objects
  } catch (error) {
    console.error('Error fetching Real-Time Stats:', error);
    throw error;
  }
};

export const setAuditTrail= async (auditType)=> {
  try {
      const response = await fetch(`${API_BASE_URL4}/setAuditTrail/${auditType}`, {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
          },
      });

      const data = await response.text();
      console.log('Audit trail set response:', data);
      return data; // Return the response
  } catch (error) {
      console.error('Error setting audit trail:', error);
  }
}

// Function to get the current value of the audit_trail
export const  getAuditTrailValue= async () => {
  try {
      const response = await fetch(`${API_BASE_URL4}/getAuditTrailValue`);
      const data = await response.text();
      console.log('Current audit trail value:', data);
      return data; // Return the value
  } catch (error) {
      console.error('Error fetching audit trail value:', error);
  }
}

// Function to restart the database
export async function restartDatabase() {
  try {
      const response = await fetch(`${API_BASE_URL4}/restart`, {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
          },
      });

      const data = await response.text();
      console.log('Database restart response:', data);
      return data; // Return the response
  } catch (error) {
      console.error('Error restarting database:', error);
  }
}

// Get tablespace encryption

export const getTablespaceEncryption = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL4}/encryption`);
    return response.data; // The JSON data from the response.
  } catch (error) {
    console.error('Error fetching tablespace encryption data:', error);
    throw error; // Re-throw the error for handling by the caller.
  }
};