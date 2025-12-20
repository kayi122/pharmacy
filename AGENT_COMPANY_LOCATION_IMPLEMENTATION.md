# Agent Company and Location Enums Implementation

## Summary
Updated the Agent model to use Company entity reference instead of a simple string, and added location selection (Province, District, Sector) during agent registration.

## Backend Changes

### 1. Agent Model (`Agent.java`)
**Changed:**
- Replaced `String companyName` field with `@ManyToOne Company company` relationship
- This allows agents to be properly linked to companies in the database

**Before:**
```java
private String companyName;
```

**After:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "company_id")
@JsonIgnore
private Company company;
```

### 2. AgentDTO (`AgentDTO.java`)
**Added:**
- `Long companyId` - to store the company ID
- `String companyName` - to display the company name

**Updated Constructor:**
- Now extracts company information from the Company entity if present

### 3. AgentService (`AgentService.java`)
**Added:**
- `CompanyRepository` dependency injection
- Company validation in `createAgent()` method
- Company validation in `updateAgent()` method

**Changes:**
- When creating/updating an agent, the service now validates and sets the Company entity reference
- Removed direct `companyName` string assignment

### 4. AgentRepository (`AgentRepository.java`)
**Updated Queries:**
- `findByCompanyNameContainingIgnoreCase` - Now uses `a.company.name` instead of `a.companyName`
- `countByCompanyName` - Now uses `a.company.name` instead of `a.companyName`
- `findAllWithRelations` - Added `LEFT JOIN FETCH a.company`
- `findByIdWithRelations` - Added `LEFT JOIN FETCH a.company`
- `findByLocationIdWithDetails` - Added `LEFT JOIN FETCH a.company`

### 5. Database Migration (`update_agent_company.sql`)
**Created SQL script to:**
- Add `company_id` column to agents table
- Add foreign key constraint to companies table
- Optionally migrate existing `company_name` data to company references
- Drop old `company_name` column after migration

## Frontend Changes

### 1. AddAgentModal Component (`App.js`)
**Complete Redesign:**

**Added State Variables:**
```javascript
const [companies, setCompanies] = useState([]);
const [provinces, setProvinces] = useState([]);
const [districts, setDistricts] = useState([]);
const [sectors, setSectors] = useState([]);
const [selectedProvince, setSelectedProvince] = useState('');
const [selectedDistrict, setSelectedDistrict] = useState('');
const [loadingLocations, setLoadingLocations] = useState(false);
```

**Updated Form Data:**
```javascript
const [formData, setFormData] = useState({
  firstName: '',
  lastName: '',
  email: '',
  phone: '',
  company: { id: '' },      // Changed from companyName string
  location: { id: '' }       // Added location reference
});
```

**New Features:**
1. **Company Dropdown** - Fetches and displays all available companies
2. **Location Cascade Selectors:**
   - Province selector (required)
   - District selector (loads based on selected province)
   - Sector selector (loads based on selected district)
3. **Loading States** - Shows loading indicator while fetching location data
4. **Validation** - All fields are required including company and location

**API Calls:**
- `/companies` - Fetches all companies on component mount
- `/locations/provinces` - Fetches all provinces on component mount
- `/locations/districts?provinceId={id}` - Fetches districts when province changes
- `/locations/sectors?districtId={id}` - Fetches sectors when district changes

## How to Use

### For Developers:

1. **Run Database Migration:**
   ```sql
   -- Execute the SQL script
   mysql -u your_user -p your_database < update_agent_company.sql
   ```

2. **Restart Backend:**
   - The backend will automatically pick up the new entity relationships
   - Make sure CompanyRepository is properly injected

3. **Test the Frontend:**
   - Navigate to Agents Management page
   - Click "Add Agent" button
   - Fill in agent details
   - Select a company from the dropdown
   - Select location (Province → District → Sector)
   - Submit the form

### For Users:

1. **Adding a New Agent:**
   - Go to Dashboard → Agents
   - Click "Add Agent" button
   - Fill in:
     - First Name (required)
     - Last Name (required)
     - Email (required)
     - Phone (required, format: 07XXXXXXXX)
     - Company (required, select from dropdown)
     - Location (required):
       - Select Province
       - Select District (loads after province selection)
       - Select Sector (loads after district selection)
   - Click "Add Agent"

2. **Viewing Agent Information:**
   - Agents list now shows company name in the "Company" column
   - Company information is fetched from the actual Company entity

## Benefits

1. **Data Integrity:**
   - Agents are now properly linked to companies via foreign key
   - Prevents orphaned or invalid company references

2. **Better Queries:**
   - Can easily fetch all agents for a specific company
   - Can join agent and company data efficiently

3. **Location Tracking:**
   - Agents now have proper location information
   - Can filter agents by province, district, or sector

4. **User Experience:**
   - Dropdown prevents typos in company names
   - Cascading location selectors ensure valid location data
   - Loading indicators provide feedback during data fetching

## API Endpoints Used

### Backend Endpoints:
- `POST /api/agents` - Create new agent
- `GET /api/companies` - Get all companies
- `GET /api/locations/provinces` - Get all provinces
- `GET /api/locations/districts?provinceId={id}` - Get districts by province
- `GET /api/locations/sectors?districtId={id}` - Get sectors by district

## Testing Checklist

- [ ] Database migration runs successfully
- [ ] Backend compiles without errors
- [ ] Can create agent with company and location
- [ ] Company dropdown shows all companies
- [ ] Province dropdown shows all provinces
- [ ] District dropdown loads when province is selected
- [ ] Sector dropdown loads when district is selected
- [ ] Form validation works (all required fields)
- [ ] Agent is saved with correct company and location references
- [ ] Agents list displays company name correctly
- [ ] Can search agents by company name

## Notes

- The location selector goes up to Sector level (Province → District → Sector)
- If you need Cell or Village level, you can extend the cascade in the same pattern
- The old `companyName` string field should be removed from the database after confirming the migration works
- Make sure to have some companies in the database before testing agent creation
