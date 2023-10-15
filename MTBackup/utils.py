import builtins


def print(string: str):
    builtins.print(string)
    with open("backup.log", "a") as file:
        builtins.print(string, file=file)


def format_size_1024(size: int) -> str:
    """Format a size with a factor of 1024"""

    for i in ["", "Ki", "Mi", "Gi", "Ti", "Pi"]:

        if size < 1024:
            return f"{size:.2f}{i}B"

        size /= 1024

    return f"{size:.2f}EiB"
