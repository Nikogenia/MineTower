from sqlalchemy import Column as Col


class Column(Col):

    inherit_cache = True

    def __init__(self, *psargs, **kwargs):
        kwargs.setdefault('nullable', False)
        super().__init__(*psargs, **kwargs)
