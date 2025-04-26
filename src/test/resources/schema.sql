-- src/test/resources/schema.sql
-- Definiciones de tablas para MySQL 8.4

CREATE TABLE IF NOT EXISTS patients (
    patient_id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
    -- otros campos si existen...
);

CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
    -- otros campos si existen...
);

CREATE TABLE IF NOT EXISTS medical_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY, -- AUTO_INCREMENT en lugar de SERIAL para MySQL
    diagnosis TEXT,
    admission_date DATE,
    patient_id INT,
    doctor_id INT,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

CREATE TABLE IF NOT EXISTS prescribed_medications (
    medication_id INT AUTO_INCREMENT PRIMARY KEY, -- AUTO_INCREMENT en lugar de SERIAL
    name VARCHAR(255),
    dosage VARCHAR(100),
    record_id INT,
    FOREIGN KEY (record_id) REFERENCES medical_records(record_id) ON DELETE CASCADE
);
