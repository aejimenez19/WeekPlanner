# Design System Document: The Zen Editorial

This design system is a high-end, bespoke framework designed for a weekly organization web application. It moves away from the rigid, grid-heavy "SaaS template" look in favor of an editorial, calm, and tactile experience. We are not just building a tool; we are building a digital sanctuary.

## 1. Overview & Creative North Star: "The Digital Curator"

The Creative North Star for this system is **The Digital Curator**. The goal is to make a task list feel like a curated gallery. We achieve this through:
- **Intentional Asymmetry:** Avoid perfectly centered, mirrored layouts. Use whitespace as a structural element to draw the eye to high-priority "exhibits" (tasks or notes).
- **Soft Minimalism:** Minimalist does not mean "empty." It means "essential." Every element must earn its place.
- **Organic Fluidity:** We use large radii (`xl` and `full`) and tonal layering to make the UI feel grown, not manufactured.

---

## 2. Colors: Tonal Depth over Structural Lines

The palette is a sophisticated blend of Sage (`primary`), Muted Blue (`secondary`), and Mineral Green (`tertiary`).

### The "No-Line" Rule
**Explicit Instruction:** Designers are prohibited from using 1px solid borders to section off content. Boundaries must be defined solely through background color shifts.
*   *Implementation:* A `surface-container-low` section sitting on a `background` surface.
*   *Why:* Borders create visual "noise" and cognitive load. Color shifts create "zones" of focus.

### Surface Hierarchy & Nesting
Treat the UI as physical layers of fine paper.
- **Base Layer:** `background` (#f9f9f7) — the canvas.
- **Sectioning:** `surface-container-low` (#f3f4f2) — for large secondary areas like sidebars.
- **Interactive Cards:** `surface-container-lowest` (#ffffff) — for the highest "lift" and focus.
- **Active States:** `surface-container-high` (#e5e9e6) — for pressed or active states.

### The "Glass & Gradient" Rule
To elevate the "Zen" aesthetic, floating elements (modals, popovers) should use **Glassmorphism**:
- **Background:** `surface` with 80% opacity.
- **Effect:** `backdrop-filter: blur(12px)`.
- **Signature Gradient:** For primary actions, use a subtle linear gradient from `primary` (#4f634f) to `primary_container` (#d2e9cf) at a 45-degree angle.

---

## 3. Typography: The Manrope Scale

We use **Manrope**, a modern sans-serif that balances geometric shapes with organic curves.

- **Display (lg/md/sm):** Used for "Zen Moments" (e.g., "Good Morning" or weekly overviews). These should be tracked slightly tighter (-0.02em) to feel editorial.
- **Headline (lg/md/sm):** For section headers. Use `on_surface_variant` (#5a605e) to keep the hierarchy soft but clear.
- **Body (lg/md/sm):** All functional text. Use `on_surface` (#2e3432) for high readability.
- **Label (md/sm):** Specifically for metadata or micro-copy. Use `on_tertiary_fixed_variant` (#506d5a) to add a hint of color to small details.

**Hierarchy Note:** Use `display-md` next to `body-lg` to create a high-contrast, "magazine-style" layout that breaks the monotony of standard web apps.

---

## 4. Elevation & Depth: Tonal Layering

Traditional shadows are often too heavy for a "Zen" aesthetic. We utilize **Tonal Layering**.

- **The Layering Principle:** Place a `surface-container-lowest` card on a `surface-container-low` background. The slight shift in brightness creates a natural "lift" without a single pixel of shadow.
- **Ambient Shadows:** If a floating element (like a mobile FAB) requires a shadow, it must be:
    - `box-shadow: 0 12px 32px rgba(46, 52, 50, 0.06);` (Tinted with `on_surface` color).
- **The "Ghost Border" Fallback:** For accessibility in input fields, use the `outline_variant` (#adb3b0) at **15% opacity**. Never use a 100% opaque border.

---

## 5. Components

### Buttons
- **Primary:** `primary` background, `on_primary` text. Use `xl` (1.5rem) rounded corners.
- **Secondary:** `secondary_container` background, `on_secondary_container` text. No border.
- **Tertiary/Ghost:** No background. `primary` text. Transitions to `surface_container_low` on hover.

### Input Fields
- **Styling:** Soft background (`surface_container_highest`), no border, `md` (0.75rem) corners.
- **Focus State:** Background shifts to `surface_container_lowest` with a subtle `primary` ghost border (20% opacity).

### Cards & Lists (The Organization Hub)
- **Rule:** **Forbid dividers.** To separate tasks in a list, use vertical spacing (`1.5rem`) or alternating `surface_container_low` and `surface_container_lowest` backgrounds.
- **Weekly View:** Use a horizontal "filmstrip" layout where each day is a `surface-container-low` column, creating a sense of progression.

### Chips (Task Tags)
- **Selection Chips:** Use `tertiary_container` with `on_tertiary_container` text. Use `full` (9999px) roundedness for a pill shape.

---

## 6. Do’s and Don’ts

### Do:
- **Do** use whitespace as a separator. If you feel the need for a line, add 16px of padding instead.
- **Do** use `primary_fixed_dim` for subtle accent backgrounds in empty states.
- **Do** lean into asymmetry—let a heading sit 40px further left than the content below it for an editorial feel.

### Don’t:
- **Don’t** use pure black (#000) or pure grey. Use the `on_surface` (#2e3432) or `on_surface_variant` for all dark tones to maintain the "organic" warmth.
- **Don’t** use the `error` (#9e422c) color for anything other than critical destructive actions. Use `secondary` for "soft" warnings.
- **Don’t** use standard `0.5rem` corners for everything. Use `xl` for large containers and `full` for buttons to emphasize the "soft" brand promise.

---

## 7. Responsive Philosophy
On mobile, the "Zen" experience is maintained by increasing vertical gutters. Instead of cramming data, use a "Single Column of Truth." Tonal layering becomes even more important here—use `surface-dim` to distinguish the bottom navigation bar from the main content scroll.