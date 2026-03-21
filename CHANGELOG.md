Support for MC 1.21.5 - 1.21.11
Add game tests to ensure functionality
Change mod structure to use stonecutter as versioning tool
Adapt chunk ticket handling for new APIs (ChunkTicketType.Use for 1.21.5+, int flags for 1.21.9+)
Fix chunk ticket serialization error on server shutdown (1.21.5+)
Fix cross-dimension entity lookup reliability in game tests
