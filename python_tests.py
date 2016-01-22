import unittest
from report_generator import get_authority_report_data
from report_generator import check_unit_id

class TestStringMethods(unittest.TestCase):
	unit_id = '999'

	def test_check_unit(self):
		self.assertTrue(check_unit_id())
	
	def test_get_authority_report_data(self):
		self.assertTrue(type(get_authority_report_data() == 'list')

	def test_upper(self):
		self.assertEqual('foo'.upper(), 'FOO')

	def test_isupper(self):
		self.assertTrue('FOO'.isupper())
		self.assertFalse('Foo'.isupper())

	def test_split(self):
		s = 'hello world'
		self.assertEqual(s.split(), ['hello', 'world'])
		# check that s.split fails when the separator is not a string
		with self.assertRaises(TypeError):
			s.split(2)

if __name__ == '__main__':
	unittest.main()