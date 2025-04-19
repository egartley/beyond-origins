import unittest
from unittest.mock import MagicMock, Mock, patch, mock_open

from pygame import Surface

from src.engine.game_state import GameState
from src.engine.level import Level


class TestLevel(unittest.TestCase):

    def setUp(self):
        mock_screen_surface = Mock(spec=Surface)
        mock_screen_surface.get_size.return_value = (1920, 1080)
        mock_game_state = Mock(spec=GameState)
        mock_game_state.screen = mock_screen_surface
        mock_level_surface = Mock(spec=Surface)
        mock_level_surface.get_size.return_value = (3000, 2000)

        self.level = Level("test", mock_game_state)
        self.level.surface = mock_level_surface
        self.level._get_tile = MagicMock(side_effect=lambda x, r, c: (f"tile_{x}", (c, r)))

    def test___init__(self):
        self.assertEqual(self.level.tiles, set())
        self.assertEqual(self.level.screen_width, 1920)
        self.assertEqual(self.level.screen_height, 1080)

    def test__set_meta(self):
        mock_file_data = "10\n20\n"
        with patch("builtins.open", mock_open(read_data=mock_file_data)):
            self.level._set_meta()
        self.assertEqual(self.level.columns, 10)
        self.assertEqual(self.level.rows, 20)

    def test__build_tiles_single(self):
        self.level.rows = 2
        self.level.columns = 3
        mock_file_data = "1\n"
        with patch("builtins.open", mock_open(read_data=mock_file_data)):
            self.level._build_tiles()
        expected_tiles = {
            ("tile_1", (c, r)) for r in range(self.level.rows) for c in range(self.level.columns)
        }
        self.assertEqual(self.level.tiles, expected_tiles)

    def test__build_tiles_full(self):
        self.level.rows = 2
        self.level.columns = 3
        mock_file_data = "1,2,3\n4,5,6\n"
        with patch("builtins.open", mock_open(read_data=mock_file_data)):
            self.level._build_tiles()
        expected_tiles = {
            ("tile_1", (0, 0)), ("tile_2", (1, 0)), ("tile_3", (2, 0)),
            ("tile_4", (0, 1)), ("tile_5", (1, 1)), ("tile_6", (2, 1))
        }
        self.assertEqual(self.level.tiles, expected_tiles)

    def test__build_tiles_wrong_row_count(self):
        self.level.rows = 2
        self.level.columns = 2
        mock_file_data = "1\n1\n1\n1\n"
        with self.assertRaises(ValueError):
            with patch("builtins.open", mock_open(read_data=mock_file_data)):
                self.level._build_tiles()

    def test__build_tiles_invalid_row(self):
        self.level.rows = 2
        self.level.columns = 2
        mock_file_data = "1\n1,2,2,2\n"
        with self.assertRaises(ValueError):
            with patch("builtins.open", mock_open(read_data=mock_file_data)):
                self.level._build_tiles()

if __name__ == '__main__':
    unittest.main()