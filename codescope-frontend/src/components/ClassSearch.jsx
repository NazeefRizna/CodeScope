import { useState } from "react";

function ClassSearch({
  onSearch,
  disabled,
  classes = [],
}) {
  const [className, setClassName] = useState("");
  const [searching, setSearching] = useState(false);
  const [showSuggestions, setShowSuggestions] = useState(false);

  const suggestions = className.trim()
    ? classes
        .filter((classInfo) =>
          classInfo.className
            .toLowerCase()
            .includes(className.trim().toLowerCase())
        )
        .slice(0, 6)
    : [];

  async function handleSearch(value = className) {
    const searchValue = value.trim();

    if (!searchValue) {
      return;
    }

    try {
      setSearching(true);
      setShowSuggestions(false);

      await onSearch(searchValue);
    } finally {
      setSearching(false);
    }
  }

  function handleSuggestionClick(classInfo) {
    setClassName(classInfo.className);
    setShowSuggestions(false);

    handleSearch(classInfo.className);
  }

  return (
    <section className="panel">
      <h2>Class Search</h2>

      <div className="search-row">

        <div className="search-input-container">

          <input
            type="text"
            value={className}
            disabled={disabled}
            placeholder="Enter a class name, for example Graph"

            onChange={(event) => {
              setClassName(event.target.value);
              setShowSuggestions(true);
            }}

            onFocus={() => {
              if (className.trim()) {
                setShowSuggestions(true);
              }
            }}

            onKeyDown={(event) => {
              if (event.key === "Enter") {
                handleSearch();
              }

              if (event.key === "Escape") {
                setShowSuggestions(false);
              }
            }}
          />

          {showSuggestions &&
            suggestions.length > 0 && (

              <div className="class-suggestions">

                {suggestions.map((classInfo) => (

                  <button
                    type="button"
                    className="class-suggestion"
                    key={classInfo.fullClassName}
                    onClick={() =>
                      handleSuggestionClick(classInfo)
                    }
                  >

                    <strong>
                      {classInfo.className}
                    </strong>

                    <span>
                      {classInfo.packageName ||
                        "Default package"}
                    </span>

                  </button>

                ))}

              </div>
            )}

        </div>

        <button
          type="button"
          className="primary-button"
          disabled={disabled || searching}
          onClick={() => handleSearch()}
        >
          {searching
            ? "Searching..."
            : "Search"}
        </button>

      </div>
    </section>
  );
}

export default ClassSearch;