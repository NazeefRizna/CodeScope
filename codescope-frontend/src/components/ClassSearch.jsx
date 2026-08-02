import { useState } from "react";

function ClassSearch({ onSearch, disabled }) {
  const [className, setClassName] = useState("");
  const [searching, setSearching] = useState(false);

  async function handleSearch() {
    const value = className.trim();

    if (!value) {
      return;
    }

    try {
      setSearching(true);
      await onSearch(value);
    } finally {
      setSearching(false);
    }
  }

  return (
    <section className="panel">
      <h2>AVL Tree Class Search</h2>

      <div className="search-row">
        <input
          type="text"
          value={className}
          disabled={disabled}
          placeholder="Enter a class name, for example Graph"
          onChange={(event) =>
            setClassName(event.target.value)
          }
          onKeyDown={(event) => {
            if (event.key === "Enter") {
              handleSearch();
            }
          }}
        />

        <button
          type="button"
          className="primary-button"
          disabled={disabled || searching}
          onClick={handleSearch}
        >
          {searching ? "Searching..." : "Search"}
        </button>
      </div>
    </section>
  );
}

export default ClassSearch;