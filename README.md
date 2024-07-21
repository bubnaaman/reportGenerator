# Report Generator Application

A Java and Spring Boot-based application for generating reports from input and reference files using configurable transformation rules.

## Features

- **Automatic Report Generation**: Generates reports every hour by default (configurable).
- **REST API Trigger**: Manually trigger report generation via a REST API endpoint.
- **Flexible File Processing**: Supports CSV files for input and reference; easily extendable for other file types.
- **Configurable Transformation Rules**: Transform input data based on defined rules, with flexibility for custom rules.

## API Endpoint - Trigger Report Generation

- **Endpoint**: `/trigger-report-generation`
- **Method**: `POST`
- **Description**: Manually triggers the report generation process.

## File Structure

### Input File (CSV)

- **Fields**:
  - `field1` (String)
  - `field2` (String)
  - `field3` (String)
  - `field4` (String)
  - `field5` (Double)
  - `refkey1` (String)
  - `refkey2` (String)

### Reference File (CSV)

- **Fields**:
  - `refkey1` (String)
  - `refdata1` (String)
  - `refkey2` (String)
  - `refdata2` (String)
  - `refdata3` (String)
  - `refdata4` (Double)

### Output File (CSV)

- **Fields**:
  - `outfield1` (String)
  - `outfield2` (String)
  - `outfield3` (String)
  - `outfield4` (Double)
  - `outfield5` (Double)

## Transformation Rules

- `outfield1 = field1 + field2`
- `outfield2 = refdata1`
- `outfield3 = refdata2 + refdata3`
- `outfield4 = field3 * max(field5, refdata4)`
- `outfield5 = max(field5, refdata4)`

## Configuration

### Application Properties

- **`report.schedule.fixedRate`**: Interval for automatic report generation in milliseconds (default: `3600000` ms = 1 hour).
- **`file.input.dir`**: Directory path for input files.
- **`file.reference.path`**: Path to the reference file.
- **`file.output.dir`**: Directory path for output files.
