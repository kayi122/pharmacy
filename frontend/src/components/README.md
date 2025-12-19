# 🎨 Reusable UI Components Library

Modern, responsive, and customizable UI components built with React and Tailwind CSS.

## 📦 Components

### Card
Flexible container with hover effects and customizable padding.
```jsx
<Card hover padding="p-6">
  <h3>Your content</h3>
</Card>
```

### Button
Stylish button with multiple variants and sizes.
```jsx
<Button variant="primary" size="md" icon={Plus}>
  Add Item
</Button>
```
**Variants:** `primary`, `secondary`, `success`, `danger`, `warning`, `outline`, `ghost`
**Sizes:** `sm`, `md`, `lg`, `xl`

### StatCard
Display statistics with icons, trends, and colors.
```jsx
<StatCard
  title="Total Revenue"
  value="RWF 125,000"
  icon={DollarSign}
  color="green"
  trend={12.5}
  description="vs last month"
/>
```
**Colors:** `blue`, `green`, `purple`, `orange`, `pink`, `teal`

### Badge
Compact status indicators with gradient options.
```jsx
<Badge variant="success" size="md">Active</Badge>
```
**Variants:** `default`, `primary`, `success`, `warning`, `danger`, `info`, `purple`, `pink`, `teal`, `indigo`

### Input
Form input with icon support and validation.
```jsx
<Input
  label="Email"
  type="email"
  value={email}
  onChange={(e) => setEmail(e.target.value)}
  icon={Mail}
  required
  error={errors.email}
/>
```

### Select
Dropdown select with custom styling.
```jsx
<Select
  label="Category"
  value={category}
  onChange={(e) => setCategory(e.target.value)}
  options={[
    { value: 'option1', label: 'Option 1' },
    { value: 'option2', label: 'Option 2' }
  ]}
  required
/>
```

### Modal
Responsive modal dialog with backdrop.
```jsx
<Modal
  isOpen={isOpen}
  onClose={() => setIsOpen(false)}
  title="Modal Title"
  size="md"
>
  <p>Modal content</p>
</Modal>
```
**Sizes:** `sm`, `md`, `lg`, `xl`, `full`

### Table
Data table with pagination and loading states.
```jsx
<Table
  columns={[
    { header: 'Name', accessor: 'name' },
    { header: 'Status', render: (row) => <Badge>{row.status}</Badge> }
  ]}
  data={data}
  loading={loading}
  pagination={{ currentPage: 0, totalPages: 5 }}
  onPageChange={handlePageChange}
/>
```

### Alert
Contextual alerts with icons.
```jsx
<Alert
  type="success"
  title="Success!"
  message="Your changes have been saved"
  onClose={() => setShowAlert(false)}
/>
```
**Types:** `success`, `error`, `warning`, `info`

### SimpleBarChart
Animated bar chart visualization.
```jsx
<SimpleBarChart
  title="Sales by Category"
  data={[
    { label: 'Category A', value: 150 },
    { label: 'Category B', value: 230 }
  ]}
  height={300}
/>
```

### SimplePieChart
Interactive pie chart with legend.
```jsx
<SimplePieChart
  title="Revenue Distribution"
  data={[
    { label: 'Direct Sales', value: 45000 },
    { label: 'Online Orders', value: 32000 }
  ]}
  size={250}
/>
```

### Pagination
Navigate between pages.
```jsx
<Pagination
  currentPage={0}
  totalPages={10}
  onPageChange={(page) => setCurrentPage(page)}
/>
```

### Skeleton
Loading placeholder animation.
```jsx
<Skeleton variant="text" count={5} />
<Skeleton variant="circle" width="64px" />
<Skeleton variant="card" />
```
**Variants:** `text`, `title`, `circle`, `rect`, `card`

### EmptyState
Display when no data is available.
```jsx
<EmptyState
  icon={Package}
  title="No products found"
  description="Add your first product to get started"
  action={() => setShowModal(true)}
  actionLabel="Add Product"
/>
```

## 🎨 Design System

### Color Palette
- **Primary:** Cyan/Blue gradient
- **Success:** Emerald/Green gradient
- **Danger:** Rose/Red gradient
- **Warning:** Amber/Orange gradient
- **Info:** Blue/Cyan gradient

### Typography
- Font: System font stack
- Weights: 400 (normal), 500 (medium), 600 (semibold), 700 (bold)

### Spacing
- Uses Tailwind's spacing scale (4px base unit)
- Consistent padding and margins across components

### Shadows
- Soft shadows for elevation
- Colored shadows for gradient buttons
- Hover states with enhanced shadows

## 📱 Responsiveness
All components are fully responsive and mobile-friendly using Tailwind's responsive utilities.

## 🚀 Usage
Import components from the index:
```jsx
import { Card, Button, StatCard, Badge } from './components';
```

Or import individually:
```jsx
import Card from './components/Card';
import Button from './components/Button';
```

## 🎭 Animations
- Smooth transitions (200ms duration)
- Hover effects with scale and shadow
- Loading states with pulse animations
- Gradient animations on interactive elements

## ♿ Accessibility
- Semantic HTML
- Keyboard navigation support
- ARIA labels where applicable
- Focus states for interactive elements
- Color contrast compliant

## 🔧 Customization
All components accept `className` prop for additional Tailwind classes and custom styling.

---

Built with ❤️ using React & Tailwind CSS
