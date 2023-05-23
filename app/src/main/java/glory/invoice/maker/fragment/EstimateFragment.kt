package glory.invoice.maker.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import glory.invoice.maker.adapter.ClientAdapter
import glory.invoice.maker.adapter.DownloadAdapter
import glory.invoice.maker.adapter.InvoiceAdapter
import glory.invoice.maker.adapter.ItemAdapter
import glory.invoice.maker.databinding.FragmentEstimateBinding
import glory.invoice.maker.viewmodel.InvoiceViewModel
import glory.invoice.maker.viewmodel.UserViewModel
import java.io.File


class EstimateFragment : Fragment() {
    lateinit var binding: FragmentEstimateBinding
    private lateinit var activityContext: Context
    private lateinit var recordingAdapter: DownloadAdapter
    private val invoiceViewModel: InvoiceViewModel by lazy {
        ViewModelProvider(this)[InvoiceViewModel::class.java]
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstimateBinding.inflate(inflater, container, false)
        activityContext = requireActivity()

        userViewModel.getUserWithInvoice().observe(activityContext as FragmentActivity) { client ->
            for (i in client) {
                binding.recyclerview.adapter = InvoiceAdapter(activityContext, i.invoice){

                }
            }
        }

        val pdfPath = activityContext.applicationInfo.dataDir
        val file = File(pdfPath)
        // this.getDir(pdfPath,Context.MODE_PRIVATE)

        val pathView: File = file
        val files: MutableList<File> = arrayListOf()
        if (pathView.isDirectory) {
            val filesOfDir = pathView.listFiles()!!
            for (i in filesOfDir) {
                if (i.isFile) {
                    files.add(i)
                }
            }
        }

        recordingAdapter = DownloadAdapter(activityContext, files)

        binding.recyclerview.adapter = recordingAdapter

        return binding.root
    }



}